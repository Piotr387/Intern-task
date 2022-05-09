package com.example.interntask;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.user.UserEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.user.UserRepository;
import com.example.interntask.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class InternTaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(InternTaskApplication.class, args);
    }

    /**
     * Initial data will execture after program fully initialize
     * @param userRepository to call method to add list of user from repository
     * @param lectureRepository to call method to add list of lectures from repository
     * @return args that will be passed to main method
     */
    @Bean
    CommandLineRunner run(UserRepository userRepository, LectureRepository lectureRepository) {
        /**
         * Initial data, list of users and list of lectures
         */
        List<LectureEntity> lectureEntityList = new ArrayList<>(Arrays.asList(
                new LectureEntity("Lecture 1 at 10:00","Frontend",  LocalTime.of(10,0)),
                new LectureEntity("Lecture 2 at 10:00","Backend", LocalTime.of(10,0)),
                new LectureEntity("Lecture 3 at 10:00","Architect", LocalTime.of(10,0)),
                new LectureEntity("Lecture 1 at 12:00","Frontend", LocalTime.of(12,0)),
                new LectureEntity("Lecture 2 at 12:00","Backend", LocalTime.of(12,0)),
                new LectureEntity("Lecture 3 at 12:00","Architect", LocalTime.of(12,0)),
                new LectureEntity("Lecture 1 at 14:00","Frontend", LocalTime.of(14,0)),
                new LectureEntity("Lecture 2 at 14:00","Backend", LocalTime.of(14,0)),
                new LectureEntity("Lecture 3 at 14:00","Architect", LocalTime.of(14,0))
        ));

        List<UserEntity> userEntityList = new ArrayList<>(Arrays.asList(
                new UserEntity("username1","mail1@mail.com"),
                new UserEntity("username2","mail2@mail.com"),
                new UserEntity("username3","mail3@mail.com"),
                new UserEntity("username4","mail4@mail.com"),
                new UserEntity("username5","mail5@mail.com"),
                new UserEntity("username6","mail6@mail.com")
        ));
        return args -> {
            /**
             * Saving inital data to our in-memory database
             */
            lectureRepository.saveAll(lectureEntityList);
            userRepository.saveAll(userEntityList);

            /**
             * Assining Lecture entity to users "hard coded" for test purpose
             */
            userEntityList.get(0).getLectureEntityList().add(lectureEntityList.get(0));
            userEntityList.get(0).getLectureEntityList().add(lectureEntityList.get(3));
            userEntityList.get(0).getLectureEntityList().add(lectureEntityList.get(6));
            userEntityList.get(1).getLectureEntityList().add(lectureEntityList.get(0));
            userEntityList.get(1).getLectureEntityList().add(lectureEntityList.get(3));
            userEntityList.get(1).getLectureEntityList().add(lectureEntityList.get(6));
            userEntityList.get(2).getLectureEntityList().add(lectureEntityList.get(1));
            userEntityList.get(2).getLectureEntityList().add(lectureEntityList.get(3));

            userRepository.saveAll(userEntityList);

        };
    }

}
