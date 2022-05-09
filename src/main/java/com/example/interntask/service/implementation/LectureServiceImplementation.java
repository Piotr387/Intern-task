package com.example.interntask.service.implementation;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.DTO.statistics.LectureStatisticsDAO;
import com.example.interntask.DTO.statistics.LectureThematicStatisticDAO;
import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.repositories.LectureRepository;
import com.example.interntask.repositories.UserRepository;
import com.example.interntask.service.LectureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LectureServiceImplementation implements LectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

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

        //Get list of all lectures and convert to LectureStatisticsDAO
        List<LectureStatisticsDAO> lectureStatisticsDAOList = lectureRepository.findAll().stream()
                .map( lecture ->
                    new LectureStatisticsDAO(
                            lecture,
                            (int)userRepository.count()))
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
