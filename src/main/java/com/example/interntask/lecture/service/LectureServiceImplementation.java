package com.example.interntask.lecture.service;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleRepository;
import com.example.interntask.user.UserEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.user.UserRepository;
import com.example.interntask.lecture.service.LectureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LectureServiceImplementation implements LectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<LectureDTO> getLectures() {
        return lectureRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<LectureDTO> getLecturesByUserLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow( () -> {
            throw new RuntimeException("Record with provided login is not found");
        });

        List<LectureDTO> lectureDTOList = userEntity.getLectureEntityList().stream()
                .map( entity -> new ModelMapper().map(entity, LectureDTO.class))
                .collect(Collectors.toList());
        return lectureDTOList;
    }

    @Override
    public List<LectureStatisticsDAO> getLecturesByPopularity() {

        RoleEntity roleEntity = roleRepository.findByName("ROLE_USER").orElseThrow( () -> {
            throw new RuntimeException("No such a role with provided name");
        });

        long listCount = userRepository.findAll().stream()
                .filter( user -> user.getRoles().contains(roleEntity))
                .count();

        //Get list of all lectures and convert to LectureStatisticsDAO
        List<LectureStatisticsDAO> lectureStatisticsDAOList = lectureRepository.findAll().stream()
                .map( lecture ->
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
        List<LectureThematicStatisticDAO> list = mapList.entrySet().stream()
                .map( entry -> new LectureThematicStatisticDAO(
                        entry.getKey(),
                        entry.getValue().doubleValue(),
                        allBusySeats))
                .collect(Collectors.toList());

        return list;
    }
}
