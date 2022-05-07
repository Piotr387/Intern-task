package com.example.interntask.service.implementation;

import com.example.interntask.DTO.UserDTO;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.repositories.LectureRepository;
import com.example.interntask.repositories.UserRepository;
import com.example.interntask.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String signUp(String lectureName, UserDTO userDTO) {
        AtomicReference<String> returnValue = new AtomicReference<>("ERROR");

        // Check for existing user by login, If user not exists in database then add to repository
        UserEntity userEntity = userRepository.findByLogin(userDTO.getLogin()).orElseGet(
                () -> userRepository.save(new ModelMapper().map(userDTO, UserEntity.class)));
        //Check if login isn't busy by other email
        if (!userEntity.getEmail().equals(userDTO.getEmail())) {
            throw new RuntimeException("Podany login jest już zajęty");
        }

        //Find lecture by name otherwise throw exception
        //If found check if user is free at this time
        lectureRepository.findByName(lectureName).ifPresentOrElse(
                lectureEntity -> {
                    boolean isUserFree = userEntity.getLectureEntityList().stream()
                            .anyMatch(lecture -> lectureEntity.getStartTime().equals(lecture.getStartTime()));
                    if (isUserFree)
                        throw new RuntimeException("User already sign in on lecture at this time");
                    if (lectureEntity.getUserEntityList().size() < lectureEntity.getCAPACITY()) {
                        userEntity.getLectureEntityList().add(lectureEntity);
                        userRepository.save(userEntity);
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
}
