package com.pp.userservice.user;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pp.amqp.RabbitMQMessageProducer;
import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureSignUpDTO;
import com.pp.userservice.lecture.dto.LectureWithFirstRegistration;
import com.pp.userservice.lecture.service.LectureService;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.OperationStatusModel;
import com.pp.userservice.response.RequestOperationName;
import com.pp.userservice.response.UserServiceException;
import com.pp.userservice.role.RoleEntity;
import com.pp.userservice.role.RoleService;
import com.pp.userservice.user.api.UserDTO;
import com.pp.userservice.user.api.UserFirstRegistration;
import com.pp.userservice.user.entity.UserEntity;
import com.pp.userservice.user.service.UserService;
import com.pp.userservice.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final Utilities utilities;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final LectureService lectureService;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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
        if (!utilities.patternMatches(userDTO.getLogin(), utilities.getUsernameRegexPattern()))
            throw new UserServiceException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
        if (!utilities.patternMatches(userDTO.getEmail(), utilities.getEmailRegexPattern()))
            throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());

        return createUserWithPassword(userDTO, utilities.generatePassword());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUserWithPassword(UserDTO userDTO, String password) {
        UserEntity userEntity = new ModelMapper().map(userDTO, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        rabbitMQMessageProducer.publish( //
                new EmailRegistrationConfirmRequest(userEntity.getLogin(), userEntity.getEmail(), password), //
                "internal.exchange", //
                "internal.email.routing-key" //
        );
        return userEntity;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OperationStatusModel signUp(LectureSignUpDTO lectureSignUpDTO) {
        userRepository.findByLogin(lectureSignUpDTO.getUserDTO().getLogin()).ifPresent(user -> {
            if (!user.getEmail().equals(lectureSignUpDTO.getUserDTO().getEmail()))
                throw new UserServiceException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
            throw new UserServiceException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
        });
        userRepository.findByEmail(lectureSignUpDTO.getUserDTO().getEmail()).ifPresent(user -> {
            throw new UserServiceException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
        });
        UserEntity userEntity = createUser(lectureSignUpDTO.getUserDTO());
        addRoleToUser(userEntity, "ROLE_USER");
        return signUpForLecture(userEntity, lectureSignUpDTO.getLectureName());
    }

    @Override
    public OperationStatusModel signUp(LectureWithFirstRegistration lectureSignUpDTO) {
        userRepository.findByLogin(lectureSignUpDTO.userDTO().login()).ifPresent(user -> {
            if (!user.getEmail().equals(lectureSignUpDTO.userDTO().email()))
                throw new UserServiceException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
            throw new UserServiceException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
        });
        userRepository.findByEmail(lectureSignUpDTO.userDTO().email()).ifPresent(user -> {
            throw new UserServiceException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
        });
        UserEntity userEntity = createUser(lectureSignUpDTO.userDTO());
        addRoleToUser(userEntity, "ROLE_USER");
        return signUpForLecture(userEntity, lectureSignUpDTO.lectureName());
    }


    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUser(UserFirstRegistration userFirstRegistration) {
        if (!utilities.patternMatches(userFirstRegistration.login(), utilities.getUsernameRegexPattern()))
            throw new UserServiceException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
        if (!utilities.patternMatches(userFirstRegistration.email(), utilities.getEmailRegexPattern()))
            throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());

        UserDTO userDTO = new UserDTO(userFirstRegistration.login(), userFirstRegistration.email());
        return createUserWithPassword(userDTO, userFirstRegistration.password());
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
        rabbitMQMessageProducer.publish( //
                new EmailSignUpConfirmationRequest(user.getLogin(), user.getEmail(), lectureName, lectureEntity.getStartTime().toString()), //
                "internal.exchange", //
                "internal.email.routing-key" //
        );


        return new OperationStatusModel.Builder(RequestOperationName.SIGN_UP_FOR_LECTURE.name()).build();
    }

    @Transactional(rollbackOn = Exception.class)
    UserEntity signUpTransaction(LectureEntity lectureEntity, UserEntity userEntity) {
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
                    if (!utilities.patternMatches(request.getParameter(newEmailFromHeader), utilities.getEmailRegexPattern()))
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
        return findUserByLogin(login).orElseThrow(() -> new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage()));
    }

    @Override
    public Optional<UserEntity> findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
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
