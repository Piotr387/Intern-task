package com.example.interntask;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleRepository;
import com.example.interntask.user.UserDTO;
import com.example.interntask.user.UserEntity;
import com.example.interntask.user.UserRepository;
import com.example.interntask.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    UserService userService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ORGANIZER");
        createRoleIfNotFound("ROLE_USER");

        /**
         * Initial data, list of users and list of lectures
         */
        List<LectureEntity> lectureEntityList = new ArrayList<>(Arrays.asList(
                new LectureEntity("Lecture 1 at 10:00", "Frontend", LocalTime.of(10, 0)),
                new LectureEntity("Lecture 2 at 10:00", "Backend", LocalTime.of(10, 0)),
                new LectureEntity("Lecture 3 at 10:00", "Architect", LocalTime.of(10, 0)),
                new LectureEntity("Lecture 1 at 12:00", "Frontend", LocalTime.of(12, 0)),
                new LectureEntity("Lecture 2 at 12:00", "Backend", LocalTime.of(12, 0)),
                new LectureEntity("Lecture 3 at 12:00", "Architect", LocalTime.of(12, 0)),
                new LectureEntity("Lecture 1 at 14:00", "Frontend", LocalTime.of(14, 0)),
                new LectureEntity("Lecture 2 at 14:00", "Backend", LocalTime.of(14, 0)),
                new LectureEntity("Lecture 3 at 14:00", "Architect", LocalTime.of(14, 0))
        ));
        lectureRepository.saveAll(lectureEntityList);

        /**
         * Adding user sign in to lecture
         * Lecutre 1 at 10:00 - 5/5
         * Lecture 2 at 12:00 - 4/5
         * Lecture 2 at 14:00 - 3/5
         */
        List<LectureSignUpDTO> userList = new ArrayList<>(Arrays.asList(
                new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username1","mail1@mail.com")),
                new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username2","mail2@mail.com")),
                new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username3","mail3@mail.com")),
                new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username4","mail4@mail.com")),
                new LectureSignUpDTO("Lecture 1 at 10:00", new UserDTO("username5","mail5@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username1","mail1@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username2","mail2@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username3","mail3@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 12:00", new UserDTO("username4","mail4@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username6","mail6@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username7","mail7@mail.com")),
                new LectureSignUpDTO("Lecture 2 at 14:00", new UserDTO("username8","mail8@mail.com"))
        ));
        userList.forEach(
                user -> {
                    userRepository.findByLogin(user.getUserDTO().getLogin()).ifPresentOrElse(
                            userEntity -> {
                                userService.signUpForLecture(userEntity, user.getLectureName());
                            },
                            () -> userService.signUp(user)
                    );
                }
        );

        /**
         * Saving one user with organizer role and other normal username
         * for test purspose (bypassing the password generator)
         */
        RoleEntity roleOrganizer = roleRepository.findByName("ROLE_ORGANIZER").orElseThrow( () -> {
            throw new RuntimeException("No role found with provided name");
        });
        UserEntity userOrganizator = userService.createUserWithpassword(new UserDTO("organizator1", "organizator1@gmail.com"), "organizator1");
        userOrganizator.getRoles().add(roleOrganizer);
        userRepository.save(userOrganizator);


        RoleEntity roleUser = roleRepository.findByName("ROLE_USER").orElseThrow( () -> {
            throw new RuntimeException("No role found with provided name");
        });
        UserEntity userTest = userService.createUserWithpassword(new UserDTO("test", "test@gmail.com"), "test");
        userTest.getRoles().add(roleUser);
        userRepository.save(userTest);

        alreadySetup = true;
    }

    @Transactional
    RoleEntity createRoleIfNotFound(String name) {

        RoleEntity roleEntity = roleRepository.findByName(name).orElseGet( () -> new RoleEntity(name));
        roleRepository.save(roleEntity);
        return roleEntity;
    }
}
