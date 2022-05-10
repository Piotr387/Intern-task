package com.example.interntask.user.service;

import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleRepository;
import com.example.interntask.user.UserDTO;
import com.example.interntask.user.UserEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.user.UserRepository;
import com.example.interntask.email.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String signUp(LectureSignUpDTO lectureSignUpDTO) {
        AtomicReference<String> returnValue = new AtomicReference<>("ERROR");

        // Check for existing user by login, If user not exists in database then add to repository
        UserEntity userEntity = userRepository.findByLogin(lectureSignUpDTO.getUserDTO().getLogin()).orElseGet(
                () -> {
                    UserEntity user = userRepository.save(new ModelMapper().map(lectureSignUpDTO.getUserDTO(), UserEntity.class));
                    addRoleToUser(user.getLogin(), "ROLE_USER");
                    return user;
                });

        //Check if login isn't taken by other email
        if (!userEntity.getEmail().equals(lectureSignUpDTO.getUserDTO().getEmail())) {
            throw new RuntimeException("Podany login jest już zajęty");
        }

        //Find lecture by name otherwise throw exception
        //If found check if user is free at this time
        lectureRepository.findByName(lectureSignUpDTO.getLectureName()).ifPresentOrElse(
                lectureEntity -> {
                    boolean isUserFree = userEntity.getLectureEntityList().stream()
                            .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));
                    if (isUserFree)
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

        return returnValue.get();
    }

    @Override
    public void cancelReservation(String userId, String letureId) {
        UserEntity userEntity = userRepository.findById(Long.valueOf(userId)).orElseThrow( () -> {
            throw new RuntimeException("No user found with provided id");
        });

        userEntity.getLectureEntityList().removeIf( lectureEntity ->
           Long.valueOf(letureId).equals(lectureEntity.getId()));

        userRepository.save(userEntity);
    }

    @Override
    public void updateEmail(String userId, String newEmail) {
        userRepository.findById(Long.valueOf(userId)).ifPresentOrElse(
                user -> {
                    if (user.getEmail().equals(newEmail))
                        throw new RuntimeException("Provided new email is same as previous one");
                    user.setEmail(newEmail);
                    userRepository.save(user);
                },
                () -> {throw new RuntimeException("No user found with provided id");}
        );
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        UserEntity userEntity = userRepository.findByLogin(username).orElseThrow(() -> {
            throw new RuntimeException("No user found with provided login");
        });
        RoleEntity roleEntity = roleRepository.findByName(roleName).orElseThrow( () -> {
            throw new RuntimeException("No role found with provided name");
        });
        userEntity.getRoles().add(roleEntity);
    }


}
