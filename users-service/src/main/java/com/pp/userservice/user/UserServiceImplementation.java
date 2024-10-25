package com.pp.userservice.user;

import static com.pp.userservice.response.ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN;

import com.pp.amqp.RabbitMQMessageService;
import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureSignUpDTO;
import com.pp.userservice.lecture.dto.LectureWithFirstRegistration;
import com.pp.userservice.lecture.service.LectureService;
import com.pp.userservice.notification.NotificationService;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.OperationStatusModel;
import com.pp.userservice.response.RequestOperationName;
import com.pp.userservice.response.UserServiceException;
import com.pp.userservice.role.RoleService;
import com.pp.userservice.security.AuthorizationUtility;
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

// start L1 Proxy - by making UserRepository package-private we're forcing to use UserServiceImplementation to get any data
// start L4 Refactored class.
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final Utilities utilities;
  private final UserRepository userRepository;
  private final RoleService roleService;
  private final LectureService lectureService;
  private final RabbitMQMessageService rabbitMQMessageService;
  private final NotificationService notificationService;
  private final AuthorizationUtility authorizationUtility;
  private final ModelMapper modelMapper = new ModelMapper();

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = findUserEntityByLogin(username, NO_USER_FOUND_WITH_PROVIDED_LOGIN);

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    userEntity.getRoles().forEach(
        role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new org.springframework.security.core.userdetails.User(userEntity.getLogin(),
        userEntity.getEncryptedPassword(), authorities);
  }

  @Override
  public List<UserDTO> getUsers() {
    return findAllUsers().stream()
        .map(entity -> new ModelMapper().map(entity, UserDTO.class))
        .toList();
  }


  @Override
  @Transactional(rollbackOn = Exception.class)
  public UserEntity createUser(UserDTO userDTO) {
    if (!utilities.patternMatches(userDTO.getLogin(), utilities.getUsernameRegexPattern())) {
      throw new UserServiceException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
    }
    if (!utilities.patternMatches(userDTO.getEmail(), utilities.getEmailRegexPattern())) {
      throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());
    }

    return createUserWithPassword(userDTO, utilities.generatePassword());
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public UserEntity createUserWithPassword(UserDTO userDTO, String password) {
    UserEntity userEntity = new ModelMapper().map(userDTO, UserEntity.class);
    userEntity.setEncryptedPassword(passwordEncoder.encode(password));
    saveUserEntity(userEntity);
    rabbitMQMessageService.sendEmailRegistrationConfirmRequest(
        new EmailRegistrationConfirmRequest(userEntity.getLogin(), userEntity.getEmail(),
            password).toString());
    return userEntity;
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public OperationStatusModel signUp(LectureSignUpDTO lectureSignUpDTO) {
    findUserByLogin(lectureSignUpDTO.getUserDTO().getLogin()).ifPresent(user -> {
      if (!user.getEmail().equals(lectureSignUpDTO.getUserDTO().getEmail())) {
        throw new UserServiceException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
      }
      throw new UserServiceException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
    });
    isEmailNotTaken(lectureSignUpDTO.getUserDTO().getEmail());
    UserEntity userEntity = createUser(lectureSignUpDTO.getUserDTO());
    addRoleToUser(userEntity, "ROLE_USER");
    return signUpForLecture(userEntity, lectureSignUpDTO.getLectureName());
  }

  @Override
  public OperationStatusModel signUp(LectureWithFirstRegistration lectureSignUpDTO) {
    userRepository.findByLogin(lectureSignUpDTO.userDTO().login()).ifPresent(user -> {
      if (!user.getEmail().equals(lectureSignUpDTO.userDTO().email())) {
        throw new UserServiceException(ErrorMessages.LOGIN_ALREADY_TAKEN.getErrorMessage());
      }
      throw new UserServiceException(ErrorMessages.ACCOUNT_ALREADY_REGISTER.getErrorMessage());
    });
    userRepository.findByEmail(lectureSignUpDTO.userDTO().email()).ifPresent(user -> {
      throw new UserServiceException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
    });
    isEmailNotTaken(lectureSignUpDTO.userDTO().email());
    UserEntity userEntity = createUser(lectureSignUpDTO.userDTO());
    addRoleToUser(userEntity, "ROLE_USER");
    return signUpForLecture(userEntity, lectureSignUpDTO.lectureName());
  }


  @Transactional(rollbackOn = Exception.class)
  public UserEntity createUser(UserFirstRegistration userFirstRegistration) {
    if (!utilities.patternMatches(userFirstRegistration.login(),
        utilities.getUsernameRegexPattern())) {
      throw new UserServiceException(ErrorMessages.LOGIN_MISS_PATTERN.getErrorMessage());
    }
    if (!utilities.patternMatches(userFirstRegistration.email(),
        utilities.getEmailRegexPattern())) {
      throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());
    }

    UserDTO userDTO = new UserDTO(userFirstRegistration.login(), userFirstRegistration.email());
    return createUserWithPassword(userDTO, userFirstRegistration.password());
  }

  @Override
  public OperationStatusModel signUpRegister(HttpServletRequest request) {
    String login = utilities.getUserLoginFromRequest(request);
    UserEntity userEntity = findUserEntityByLogin(login, NO_USER_FOUND_WITH_PROVIDED_LOGIN);
    return signUpForLecture(userEntity, request.getParameter("lectureName"));
  }

  @Override
  public OperationStatusModel signUpForLecture(UserEntity userEntity, String lectureName) {

    LectureEntity lectureEntity = lectureService.findByName(lectureName);

    boolean isUserBusy = userEntity.getLectureEntityList().stream()
        .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));

    if (isUserBusy) {
      throw new UserServiceException(ErrorMessages.USER_TAKEN_AT_THIS_HOUR.getErrorMessage());
    }

    if (lectureEntity.getUserEntityList().size() >= lectureEntity.getCAPACITY()) {
      throw new UserServiceException(ErrorMessages.NO_FREE_SEATS_AT_LECTURE.getErrorMessage());
    }

    UserEntity user = signUpTransaction(lectureEntity, userEntity);
    if (user == null) {
      throw new UserServiceException(ErrorMessages.SOMETHING_WENT_WRONG.getErrorMessage());
    }

    var payload = new EmailSignUpConfirmationRequest(user.getLogin(), user.getEmail(), lectureName,
        lectureEntity.getStartTime().toString());
    rabbitMQMessageService.sendEmailRegistrationConfirmRequest(payload.toString());
    notificationService.sendNotification(userEntity, payload.toString());

    return new OperationStatusModel.Builder(
        RequestOperationName.SIGN_UP_FOR_LECTURE.name()).build();
  }

  @Transactional(rollbackOn = Exception.class)
  public UserEntity signUpTransaction(LectureEntity lectureEntity, UserEntity userEntity) {
    userEntity.getLectureEntityList().add(lectureEntity);
    return saveUserEntity(userEntity);
  }


  @Override
  public OperationStatusModel cancelReservation(HttpServletRequest request) {
    String login = utilities.getUserLoginFromRequest(request);
    UserEntity userEntity = findUserEntityByLogin(login, NO_USER_FOUND_WITH_PROVIDED_LOGIN);

    boolean isDelete = userEntity.getLectureEntityList().removeIf(lectureEntity ->
        request.getParameter("lectureName").equals(lectureEntity.getName()));
    if (!isDelete) {
      return new OperationStatusModel.Builder(RequestOperationName.CANCEL_RESERVATION.name())
          .operationResult(ErrorMessages.DELETE_ERROR.getErrorMessage()).build();
    }

    saveUserEntity(userEntity);
    return new OperationStatusModel.Builder(RequestOperationName.CANCEL_RESERVATION.name()).build();
  }

  @Override
  public void updateEmail(HttpServletRequest request) {
    String login = utilities.getUserLoginFromRequest(request);
    findUserByLogin(login).ifPresentOrElse(
        user -> {
          String newEmailFromHeader = "newEmail";
          if (user.getEmail().equals(request.getParameter(newEmailFromHeader))) {
            throw new UserServiceException(
                ErrorMessages.EMAIL_ERROR_SAME_AS_PREVIOUS.getErrorMessage());
          }
          if (!utilities.patternMatches(request.getParameter(newEmailFromHeader),
              utilities.getEmailRegexPattern())) {
            throw new UserServiceException(ErrorMessages.EMAIL_MISS_PATTERN.getErrorMessage());
          }
          user.setEmail(request.getParameter(newEmailFromHeader));
          saveUserEntity(user);
        },
        () -> {
          throw new UserServiceException(
              NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        }
    );
  }

  @Override
  public void addRoleToUser(UserEntity userEntity, String roleName) {
    userEntity.getRoles().add(roleService.getByRoleName(roleName));
    saveUserEntity(userEntity);
  }

  @Override
  public UserEntity getUser(String login) {
    return findUserEntityByLogin(login, NO_USER_FOUND_WITH_PROVIDED_LOGIN);
  }

  @Override
  public void saveUser(UserEntity userEntity) {
    saveUserEntity(userEntity);
  }

  @Override
  public List<UserEntity> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void getRefreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    authorizationUtility.getRefreshToken(request, response, this::findUserEntityByLogin);
  }

  @Override
  public List<LectureDTO> getUserLecture(HttpServletRequest request) {
    String login = utilities.getUserLoginFromRequest(request);
    UserEntity userEntity = findUserEntityByLogin(login);
    return userEntity.getLectureEntityList().stream()
        .map(entity -> new ModelMapper().map(entity, LectureDTO.class)).toList();
  }

  @Override
  public UserDTO getUserAccountDetails(HttpServletRequest request) {

    String login = utilities.getUserLoginFromRequest(request);
    UserEntity userEntity = findUserEntityByLogin(login);
    return modelMapper.map(userEntity, UserDTO.class);
  }

  private UserEntity findUserEntityByLogin(String login) {

    return findUserEntityByLogin(login, NO_USER_FOUND_WITH_PROVIDED_LOGIN);
  }

  private UserEntity findUserEntityByLogin(String login, ErrorMessages errorMessages) {

    return findUserByLogin(login).orElseThrow(() -> //
        new UserServiceException(errorMessages.getErrorMessage())
    );
  }

  @Override
  public Optional<UserEntity> findUserByLogin(String login) {
    return userRepository.findByLogin(login);
  }

  private void isEmailNotTaken(String email) {

    userRepository.findByEmail(email).ifPresent(user -> {
      throw new UserServiceException(ErrorMessages.EMAIL_ALREADY_TAKEN.getErrorMessage());
    });
  }

  private UserEntity saveUserEntity(UserEntity userEntity) {

    return userRepository.save(userEntity);
  }
}
