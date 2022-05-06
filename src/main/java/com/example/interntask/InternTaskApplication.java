package com.example.interntask;

import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.repositories.LectureRepository;
import com.example.interntask.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class InternTaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(InternTaskApplication.class, args);

        /**
         * Initial data, list of users and list of lectures
         */
        List<UserEntity> userEntityList = new ArrayList<>(Arrays.asList(
                new UserEntity("username1","mail1@mail.com"),
                new UserEntity("username2","mail2@mail.com"),
                new UserEntity("username3","mail3@mail.com"),
                new UserEntity("username4","mail4@mail.com"),
                new UserEntity("username5","mail5@mail.com"),
                new UserEntity("username6","mail6@mail.com")
        ));
        List<LectureEntity> lectureEntityList = new ArrayList<>(Arrays.asList(
                new LectureEntity("Lecture 1 at 10:00", LocalTime.of(10,0)),
                new LectureEntity("Lecture 2 at 10:00", LocalTime.of(10,0)),
                new LectureEntity("Lecture 3 at 10:00", LocalTime.of(10,0)),
                new LectureEntity("Lecture 1 at 12:00", LocalTime.of(12,0)),
                new LectureEntity("Lecture 2 at 12:00", LocalTime.of(12,0)),
                new LectureEntity("Lecture 3 at 12:00", LocalTime.of(12,0)),
                new LectureEntity("Lecture 1 at 14:00", LocalTime.of(14,0)),
                new LectureEntity("Lecture 2 at 14:00", LocalTime.of(14,0)),
                new LectureEntity("Lecture 3 at 14:00", LocalTime.of(14,0))
        ));
        /**
         * Saving inital data to our in-memory database
         */
        LectureRepository lectureRepository =
                configurableApplicationContext.getBean(LectureRepository.class);
        lectureRepository.saveAll(lectureEntityList);

        UserRepository userRepository =
                configurableApplicationContext.getBean(UserRepository.class);
        userRepository.saveAll(userEntityList);

    }

}
