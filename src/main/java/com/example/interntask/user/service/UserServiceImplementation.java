package com.example.interntask.user.service;

import com.example.interntask.email.EmailService;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleRepository;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
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
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LectureService lectureService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() -> {
            throw new RuntimeException("No user name with provided login");
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
        return createUserWithpassword(userDTO, utilities.generatePassword());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserEntity createUserWithpassword(UserDTO userDTO, String password) {
        UserEntity userEntity = new ModelMapper().map(userDTO, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        emailService.sendInvitationEmail(userEntity, password);
        return userEntity;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String signUp(LectureSignUpDTO lectureSignUpDTO) {
        AtomicReference<String> returnValue = new AtomicReference<>("ERROR");
        userRepository.findByLogin(lectureSignUpDTO.getUserDTO().getLogin()).ifPresentOrElse(
                //If user will be find, check if email matches if no throw expection,
                // if yes ten give user information to sign up to lecture from user account
                userEntity -> {
                    if (!userEntity.getEmail().equals(lectureSignUpDTO.getUserDTO().getEmail()))
                        throw new RuntimeException("Podany login jest już zajęty");

                    throw new RuntimeException("Znaleziono konto o takich danych. Zapisz się na prelekcje z poziomu konta");
                },
                //if user don't exists, it will check if email address is free and create user,
                // otherwise throw exception
                () -> {
                    UserEntity user = createUser(lectureSignUpDTO.getUserDTO());
                    addRoleToUser(user, "ROLE_USER");
                    returnValue.set(signUpForLecture(user, lectureSignUpDTO.getLectureName()));
                }
        );
        return String.valueOf(returnValue);
    }

    @Override
    public String signUpRegister(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new RuntimeException("No user with provided login");
        });
        return signUpForLecture(userEntity, request.getParameter("lectureName"));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String signUpForLecture(UserEntity userEntity, String lectureName) {
        AtomicReference<String> returnValue = new AtomicReference<>("ERROR");
        lectureRepository.findByName(lectureName).ifPresentOrElse(
                lectureEntity -> {
                    boolean isUserBusy = userEntity.getLectureEntityList().stream()
                            .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));
                    if (isUserBusy)
                        throw new RuntimeException("User already sign in on lecture at this time");

                    if (lectureEntity.getUserEntityList().size() < lectureEntity.getCAPACITY()) {
                        userEntity.getLectureEntityList().add(lectureEntity);
                        userRepository.save(userEntity);
                        emailService.sendEmail(userEntity, lectureEntity);
                        returnValue.set("SUCCES");
                    } else {
                        throw new RuntimeException("Brak wolnych miejsc w wybranym wydarzeniu");
                    }
                },
                () -> {
                    throw new RuntimeException("Nie znaleziono prelekcji o podanej nazwie");
                }
        );
        return String.valueOf(returnValue);
    }

    @Override
    public void cancelReservation(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new RuntimeException("No user found with provided id");
        });

        userEntity.getLectureEntityList().removeIf(lectureEntity ->
                request.getParameter("lectureName").equals(lectureEntity.getName()));

        userRepository.save(userEntity);
    }

    @Override
    public void updateEmail(HttpServletRequest request) {
        String login = utilities.getUserLoginFromRequest(request);
        userRepository.findByLogin(login).ifPresentOrElse(
                user -> {
                    if (user.getEmail().equals(request.getParameter("newEmail")))
                        throw new RuntimeException("Provided new email is same as previous one");
                    user.setEmail(request.getParameter("newEmail"));
                    userRepository.save(user);
                },
                () -> {
                    throw new RuntimeException("No user found with provided id");
                }
        );
    }

    @Override
    public void addRoleToUser(UserEntity userEntity, String roleName) {
        RoleEntity roleEntity = roleRepository.findByName(roleName).orElseThrow(() -> {
            throw new RuntimeException("No role found with provided name");
        });
        userEntity.getRoles().add(roleEntity);
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUser(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> {
            throw new RuntimeException("No such a user with provided login");
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
            throw new RuntimeException("Refresh token is missing");
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
            throw new RuntimeException("No such a user with provided login");
        });
        return new ModelMapper().map(userEntity, UserDTO.class);
    }


}
