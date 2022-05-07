package com.example.interntask.service.implementation;

import com.example.interntask.DTO.LectureSignUpDTO;
import com.example.interntask.DTO.UserDTO;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.repositories.LectureRepository;
import com.example.interntask.repositories.UserRepository;
import com.example.interntask.service.EmailService;
import com.example.interntask.service.UserService;
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
                () -> userRepository.save(new ModelMapper().map(lectureSignUpDTO.getUserDTO(), UserEntity.class)));

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
}
