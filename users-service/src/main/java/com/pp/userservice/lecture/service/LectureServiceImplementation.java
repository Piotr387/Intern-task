package com.pp.userservice.lecture.service;


import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expression;
import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions.AndExpression;
import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions.OrExpression;
import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions.StartTimeExpression;
import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions.ThematicPathExpression;
import com.pp.userservice.lecture.DesignPatterns.Iterator.Iterator;
import com.pp.userservice.lecture.DesignPatterns.Iterator.LectureCollection;
import com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies.ArchitectStrategy;
import com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies.BackendStrategy;
import com.pp.userservice.lecture.DesignPatterns.Strategy.Strategies.FrontendStrategy;
import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.LectureRepository;
import com.pp.userservice.lecture.dto.*;
import com.pp.userservice.responde.ErrorMessages;
import com.pp.userservice.responde.UserServiceException;
import com.pp.userservice.role.RoleEntity;
import com.pp.userservice.role.RoleService;
import com.pp.userservice.user.UserEntity;
import com.pp.userservice.user.UserLoginWithEmail;
import com.pp.userservice.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public List<LectureDTO> getLectures() {
        return lectureRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList();
    }

    public List<LectureDetailsWithUser> getLecturesWithUser() {

        return lectureRepository.findAll().stream()
                .map(this::convertEntityTOLecturesWithUser)
                .toList();
    }

    private LectureDetailsWithUser convertEntityTOLecturesWithUser(LectureEntity lectureEntity) {

        var userLoginAndPassword = lectureEntity.getUserEntityList().stream().map(entity -> new UserLoginWithEmail(entity.getLogin(), entity.getEmail())).toList();

        return new LectureDetailsWithUser(
                String.valueOf(lectureEntity.getId()), //
                lectureEntity.getName(), //
                lectureEntity.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME), //
                lectureEntity.getThematicPath(), //
                userLoginAndPassword //
        );
    }

    @Override
    public LectureEntity findByName(String name) {
        return lectureRepository.findByName(name).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.LECTURE_NOT_FOUND_BY_NAME.getErrorMessage());
        });
    }

    @Override
    public List<LectureDetailsDTO> getLecturesDetails() {
        return lectureRepository.findAll().stream()
                .map(entity ->
                        new LectureDetailsDTO(
                                new ModelMapper().map(entity, LectureDTO.class),
                                entity.getCAPACITY(),
                                entity.getUserEntityListSize()
                        ))
                .toList();
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

        return lectureRepository.findAll().stream()
                .map(lecture ->
                        new LectureStatisticsDAO(
                                lecture,
                                listCount))
                .sorted(
                        Comparator.comparing(LectureStatisticsDAO::getBusySeatsOverAllUsers).reversed()
                                .thenComparing(LectureStatisticsDAO::getSeatTaken)
                                .thenComparing(LectureStatisticsDAO::getSeatCapacity)
                ).toList();
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

    //COMMAND IMPLEMENTED
    @Override
    public void deleteLecture(String lectureName) {
        lectureRepository.deleteByThematicPath(lectureName);
    }


    //EXPRESSION IMPLEMENTED
    @Override
    public List<LectureDTO> getLecturesByExpressions(String thematicPath1, String thematicPath2, String startTime) {
        ModelMapper modelMapper = new ModelMapper();

        Expression themePathExpression = new OrExpression(
                new ThematicPathExpression(thematicPath1),
                new ThematicPathExpression(thematicPath2)
        );

        Expression finalExpression = new AndExpression(themePathExpression, new StartTimeExpression(startTime));

        return lectureRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, LectureDTO.class))
                .filter(finalExpression::interpret)
                .toList();
    }

    //ITERATOR IMPLEMENTED
    public List<LectureIteratedDTO> getLecturesIterated(){
        LectureCollection lectureCollection = new LectureCollection(
                lectureRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList()
        );

        Iterator<LectureDTO> iterator = lectureCollection.createIterator();
        List<LectureIteratedDTO> result = new ArrayList<>();

        int i = 1;
        while (iterator.hasNext()) {
            LectureDTO lectureDTO = iterator.next();
            result.add(new LectureIteratedDTO(i, lectureDTO.getName(), lectureDTO.getThematicPath(), lectureDTO.getStartTime()));
            i ++;
        }

        return result;
    }

    //STRATEGY IMPLEMENTED
    public List<Map<String, List<String>>> getLecureSyllabus(String thematicPath){
        return switch (thematicPath) {
            case "frontend" -> new FrontendStrategy().show();
            case "backend" -> new BackendStrategy().show();
            case "architect" -> new ArchitectStrategy().show();
            default -> throw new IllegalArgumentException("Unexpected value: " + thematicPath);
        };
    };
}
