package com.example.interntask.user.service;

import com.example.interntask.email.EmailService;
import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.responde.ErrorMessages;
import com.example.interntask.responde.OperationStatusModel;
import com.example.interntask.responde.RequestOperationName;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleService;
import com.example.interntask.user.UserDTO;
import com.example.interntask.user.UserEntity;
import com.example.interntask.user.UserRepository;
import com.example.interntask.utils.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    Utilities utilities;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    RoleService roleService;

    @Autowired
    LectureService lectureService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() -> {
            throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
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
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUser(UserDTO userDTO) {
        if (!utilities.patternMatches(userDTO.getLogin(),utilities.getUsernameRegexPattern()))
            throw new RuntimeException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
        if (!utilities.patternMatches(userDTO.getEmail(),utilities.getEmailRegexPattern()))
            throw new RuntimeException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());

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
                throw new RuntimeException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
            throw new RuntimeException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
        });
        userRepository.findByEmail(lectureSignUpDTO.getUserDTO().getEmail()).ifPresent( user ->{
            throw new RuntimeException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
        });
        UserEntity userEntity = createUser(lectureSignUpDTO.getUserDTO());
        addRoleToUser(userEntity, "ROLE_USER");
        return signUpForLecture(userEntity, lectureSignUpDTO.getLectureName());
    }

    @Override
    public OperationStatusModel signUpRegister(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
        return signUpForLecture(userEntity, request.getParameter("lectureName"));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OperationStatusModel signUpForLecture(UserEntity userEntity, String lectureName) {

        LectureEntity lectureEntity = lectureService.findByName(lectureName);

        boolean isUserBusy = userEntity.getLectureEntityList().stream()
                .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));

        if (isUserBusy)
            throw new RuntimeException(ErrorMessages.USER_TAKEN_AT_THIS_HOUR.getErrorMessage());

        if (lectureEntity.getUserEntityList().size() >= lectureEntity.getCAPACITY())
            throw new RuntimeException(ErrorMessages.NO_FREE_SEATS_AT_LECTURE.getErrorMessage());

        userEntity.getLectureEntityList().add(lectureEntity);
        userRepository.save(userEntity);
        emailService.sendEmail(userEntity, lectureEntity);

        return new OperationStatusModel.Builder(RequestOperationName.SIGN_UP_FOR_LECTURE.name()).build();
    }

    @Override
    public OperationStatusModel cancelReservation(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
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
                    if (user.getEmail().equals(request.getParameter("newEmail")))
                        throw new RuntimeException(ErrorMessages.EMAIL_ERROR_SAME_AS_PREVIOUS.getErrorMessage());
                    if (!utilities.patternMatches(request.getParameter("newEmail"),utilities.getEmailRegexPattern()))
                        throw new RuntimeException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());
                    user.setEmail(request.getParameter("newEmail"));
                    userRepository.save(user);
                },
                () -> {
                    throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
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
            throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
    }

    @Override
    public void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                String login = utilities.getUserLoginFromToken(refresh_token);

                UserEntity userEntity = getUser(login);

                String access_token = utilities.createAccessToken(userEntity.getLogin(),
                        request.getRequestURL().toString(),
                        userEntity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toList()));

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),
                        utilities.createMapOfTokens(access_token, refresh_token));
            } catch (Exception e) {
                utilities.tokenCreatingException(e, response);
            }
        } else {
            throw new RuntimeException(ErrorMessages.REFRESH_TOKEN_MISSING.getErrorMessage());
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
            throw new RuntimeException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });
        return new ModelMapper().map(userEntity, UserDTO.class);
    }


}
