package com.pp.userservice.lecture.service;


import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.LectureRepository;
import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureDetailsDTO;
import com.pp.userservice.lecture.dto.LectureDetailsWithUser;
import com.pp.userservice.lecture.dto.LectureStatisticsDAO;
import com.pp.userservice.lecture.dto.LectureThematicStatisticDAO;
import com.pp.userservice.response.ErrorMessages;
import com.pp.userservice.response.UserServiceException;
import com.pp.userservice.role.RoleEntity;
import com.pp.userservice.role.RoleService;
import com.pp.userservice.user.api.UserLoginWithEmail;
import com.pp.userservice.user.entity.UserEntity;
import com.pp.userservice.user.service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LectureServiceImplementation implements LectureService {

    private final LectureRepository lectureRepository;
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
        return lectureRepository.findByName(name).orElseThrow(() -> new UserServiceException(ErrorMessages.LECTURE_NOT_FOUND_BY_NAME.getErrorMessage()));
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

        Predicate<LectureEntity> hasLectureSignedUserWithSearchedLogin = lectureEntity -> lectureEntity.getUserEntityList().stream().anyMatch(user -> login.equals(user.getLogin()));

        return lectureRepository.findAll().stream().filter(Predicate.not(hasLectureSignedUserWithSearchedLogin))
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList();
    }

    @Override
    public List<LectureStatisticsDAO> getLecturesByPopularity() {

        RoleEntity roleEntity = roleService.findByName("ROLE_USER");

        var lectures = lectureRepository.findAll();

        var totalUserCount = lectures.stream()
                .map(LectureEntity::getUserEntityList)
                .flatMap(Collection::stream)
                .distinct()
                .filter(user -> user.getRoles().contains(roleEntity))
                .count();

        return lectures.stream()
                .map(lecture ->
                        new LectureStatisticsDAO(
                                lecture,
                                totalUserCount))
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
                        Integer::sum));
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
