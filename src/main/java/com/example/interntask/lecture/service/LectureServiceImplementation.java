package com.example.interntask.lecture.service;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.responde.ErrorMessages;
import com.example.interntask.responde.UserServiceException;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleService;
import com.example.interntask.user.UserEntity;
import com.example.interntask.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LectureServiceImplementation implements LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public List<LectureDTO> getLectures() {
        return lectureRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList();
    }

    @Override
    public LectureEntity findByName(String name) {
        return lectureRepository.findByName(name).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.LECTURE_NOT_FOUND_BY_NAME.getErrorMessage());
        });
    }

    @Override
    public List<LectureDTO> getLecturesByUserLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_USER_FOUND_WITH_PROVIDED_LOGIN.getErrorMessage());
        });

        return userEntity.getLectureEntityList().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList();
    }

    @Override
    public List<LectureStatisticsDAO> getLecturesByPopularity() {

        RoleEntity roleEntity = roleService.findByName("ROLE_USER");

        long listCount = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(roleEntity))
                .count();

        //Get list of all lectures and convert to LectureStatisticsDAO
        List<LectureStatisticsDAO> lectureStatisticsDAOList = lectureRepository.findAll().stream()
                .map(lecture ->
                        new LectureStatisticsDAO(
                                lecture,
                                listCount))
                .collect(Collectors.toList());

        //Sort by busySeats/
        lectureStatisticsDAOList.sort(
                Comparator.comparing(LectureStatisticsDAO::getBusySeatsOverAllUsers).reversed()
                        .thenComparing(LectureStatisticsDAO::getSeatTaken)
                        .thenComparing(LectureStatisticsDAO::getSeatCapacity));

        return lectureStatisticsDAOList;
    }

    @Override
    public List<LectureThematicStatisticDAO> getLecturesByThematicPathPopularity() {

        //Implement count busy seats in every category
        // over all busy seats in all categories = sum should be 100%

        //List of all busy seats in each category
        Map<String, Integer> mapList = lectureRepository.findAll().stream()
                .collect(Collectors.toMap(
                        LectureEntity::getThematicPath,
                        LectureEntity::getUserEntityListSize,
                        (previousValue, nextValue) -> previousValue + nextValue));
        //Sum all above integers
        int allBusySeats = mapList.values().stream().reduce(0, Integer::sum);

        //Create new class with each category and percentage
        return mapList.entrySet().stream()
                .map(entry -> new LectureThematicStatisticDAO(
                        entry.getKey(),
                        entry.getValue().doubleValue(),
                        allBusySeats))
                .toList();
    }
}
