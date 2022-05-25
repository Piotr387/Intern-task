package com.example.interntask.user.service;

import com.example.interntask.email.EmailService;
import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.responde.ErrorMessages;
import com.example.interntask.responde.OperationStatusModel;
import com.example.interntask.responde.RequestOperationName;
import com.example.interntask.responde.UserServiceException;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleService;
import com.example.interntask.user.UserDTO;
import com.example.interntask.user.UserEntity;
import com.example.interntask.user.UserRepository;
import com.example.interntask.utils.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Utilities utilities;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleService roleService;
    private final LectureService lectureService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getRoles().forEach(
                role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(userEntity.getLogin(), userEntity.getEncryptedPassword(), authorities);
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, UserDTO.class))
                .toList();
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUser(UserDTO userDTO) {
        if (!utilities.patternMatches(userDTO.getLogin(),utilities.getUsernameRegexPattern()))
            throw new UserServiceException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
        if (!utilities.patternMatches(userDTO.getEmail(),utilities.getEmailRegexPattern()))
            throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());

        return createUserWithPassword(userDTO, utilities.generatePassword());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUserWithPassword(UserDTO userDTO, String password) {
        UserEntity userEntity = new ModelMapper().map(userDTO, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        emailService.sendInvitationEmail(userEntity, password);
        return userEntity;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OperationStatusModel signUp(LectureSignUpDTO lectureSignUpDTO) {
        userRepository.findByLogin(lectureSignUpDTO.getUserDTO().getLogin()).ifPresent( user -> {
            if (!user.getEmail().equals(lectureSignUpDTO.getUserDTO().getEmail()))
                throw new UserServiceException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
            throw new UserServiceException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
        });
        userRepository.findByEmail(lectureSignUpDTO.getUserDTO().getEmail()).ifPresent( user ->{
            throw new UserServiceException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
        });
        UserEntity userEntity = createUser(lectureSignUpDTO.getUserDTO());
        addRoleToUser(userEntity, "ROLE_USER");
        return signUpForLecture(userEntity, lectureSignUpDTO.getLectureName());
    }

    @Override
    public OperationStatusModel signUpRegister(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
        return signUpForLecture(userEntity, request.getParameter("lectureName"));
    }

    @Override
    public OperationStatusModel signUpForLecture(UserEntity userEntity, String lectureName) {

        LectureEntity lectureEntity = lectureService.findByName(lectureName);

        boolean isUserBusy = userEntity.getLectureEntityList().stream()
                .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));

        if (isUserBusy)
            throw new UserServiceException(ErrorMessages.USER_TAKEN_AT_THIS_HOUR.getErrorMessage());

        if (lectureEntity.getUserEntityList().size() >= lectureEntity.getCAPACITY())
            throw new UserServiceException(ErrorMessages.NO_FREE_SEATS_AT_LECTURE.getErrorMessage());

        UserEntity user = signUpTransaction(lectureEntity, userEntity);
        if (user == null)
            throw new UserServiceException(ErrorMessages.SOMETHING_WENT_WRONG.getErrorMessage());
        emailService.sendEmail(userEntity, lectureEntity);

        return new OperationStatusModel.Builder(RequestOperationName.SIGN_UP_FOR_LECTURE.name()).build();
    }

    @Transactional(rollbackOn = Exception.class)
    UserEntity signUpTransaction(LectureEntity lectureEntity, UserEntity userEntity){
        userEntity.getLectureEntityList().add(lectureEntity);
        return userRepository.save(userEntity);
    }


    @Override
    public OperationStatusModel cancelReservation(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });

        boolean isDelete = userEntity.getLectureEntityList().removeIf(lectureEntity ->
                request.getParameter("lectureName").equals(lectureEntity.getName()));
        if (!isDelete)
            return new OperationStatusModel.Builder(RequestOperationName.CANCEL_RESERVATION.name())
                    .operationResult(ErrorMessages.DELETE_ERROR.getErrorMessage()).build();

        userRepository.save(userEntity);
        return new OperationStatusModel.Builder(RequestOperationName.CANCEL_RESERVATION.name()).build();
    }

    @Override
    public void updateEmail(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        userRepository.findByLogin(login).ifPresentOrElse(
                user -> {
                    String newEmailFromHeader = "newEmail";
                    if (user.getEmail().equals(request.getParameter(newEmailFromHeader)))
                        throw new UserServiceException(ErrorMessages.EMAIL_ERROR_SAME_AS_PREVIOUS.getErrorMessage());
                    if (!utilities.patternMatches(request.getParameter(newEmailFromHeader),utilities.getEmailRegexPattern()))
                        throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());
                    user.setEmail(request.getParameter(newEmailFromHeader));
                    userRepository.save(user);
                },
                () -> {
                    throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
                }
        );
    }

    @Override
    public void addRoleToUser(UserEntity userEntity, String roleName) {
        userEntity.getRoles().add(roleService.findByName(roleName));
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUser(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
    }

    @Override
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                String login = utilities.getUserLoginFromToken(refreshToken);

                UserEntity userEntity = getUser(login);

                String accessToken = utilities.createAccessToken(userEntity.getLogin(),
                        request.getRequestURL().toString(),
                        userEntity.getRoles().stream().map(RoleEntity::getName).toList());

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        utilities.createMapOfTokens(accessToken, refreshToken));
            } catch (Exception e) {
                utilities.tokenCreatingException(e, response);
            }
        } else {
            throw new UserServiceException(ErrorMessages.REFRESH_TOKEN_MISSING.getErrorMessage());
        }
    }

    @Override
    public List<LectureDTO> getUserLecture(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        return lectureService.getLecturesByUserLogin(login);
    }

    @Override
    public UserDTO getUserAccountDetails(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
        return new ModelMapper().map(userEntity, UserDTO.class);
    }


}
