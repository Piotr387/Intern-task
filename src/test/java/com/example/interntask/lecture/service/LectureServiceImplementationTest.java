package com.example.interntask.lecture.service;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.lecture.LectureRepository;
import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.responde.UserServiceException;
import com.example.interntask.role.RoleEntity;
import com.example.interntask.role.RoleService;
import com.example.interntask.user.UserEntity;
import com.example.interntask.user.UserRepository;
import org.hibernate.boot.model.source.spi.Sortable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import javax.management.relation.Role;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LectureServiceImplementationTest {

    private static LectureServiceImplementation lectureServiceImplementation;
    private static LectureRepository lectureRepository;
    private static UserRepository userRepository;
    private static RoleService roleService;

    private static RoleEntity roleUser;
    private static RoleEntity roleOrganizer;

    @BeforeAll
    public static void setUp() {
        lectureRepository = mock(LectureRepository.class);
        userRepository = mock(UserRepository.class);
        roleService = mock(RoleService.class);
        lectureServiceImplementation = new LectureServiceImplementation(lectureRepository, userRepository, roleService);
        roleUser = new RoleEntity("ROLE_USER");
        roleOrganizer = new RoleEntity("ROLE_ORGANIZER");
    }

    @Test
    void getLectures() {
        List<LectureEntity> lectureEntityList = getLecturesEntity();
        when(lectureRepository.findAll()).thenReturn(lectureEntityList);

        List<LectureDTO> lectureDtoList = lectureServiceImplementation.getLectures();

        assertNotNull(lectureDtoList);
        assertEquals(lectureEntityList.size(), lectureDtoList.size());
        assertFalse(lectureDtoList.isEmpty());
        assertTrue(lectureDtoList.containsAll(lectureEntityList.stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .toList()));

    }

    @Test
    void getLecturesEmptyArr() {
        when(lectureRepository.findAll()).thenReturn(new ArrayList<>(0));

        List<LectureDTO> lectureDtoList = lectureServiceImplementation.getLectures();

        assertNotNull(lectureDtoList);
        assertTrue(lectureDtoList.isEmpty());

    }

    @Test
    void findByNameIfFound() {
        LectureEntity lectureEntity = getLecturesEntity().get(0);
        when(lectureRepository.findByName(anyString())).thenReturn(Optional.of(lectureEntity));

        LectureEntity returnedLectureEntity = lectureServiceImplementation.findByName(anyString());

        assertNotNull(returnedLectureEntity);
        assertEquals(lectureEntity.getName(), returnedLectureEntity.getName());
        assertEquals(lectureEntity, returnedLectureEntity);
    }

    @Test
    void findByNameIfNotFound() {
        when(lectureRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThrows(UserServiceException.class, () -> lectureServiceImplementation.findByName(anyString()));
    }

    @Test
    void getLecturesByUserLoginIfFoundUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setLectureEntityList(getLecturesEntity());
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(userEntity));

        List<LectureDTO> lectureDTOList = lectureServiceImplementation.getLecturesByUserLogin(anyString());

        assertNotNull(lectureDTOList);
        assertEquals(lectureDTOList, userEntity.getLectureEntityList().stream()
                .map(lectureEntity -> new ModelMapper().map(lectureEntity, LectureDTO.class))
                .toList());
    }

    @Test
    void getLecturesByUserLoginIfNotFoundUser() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        assertThrows(UserServiceException.class, () -> lectureServiceImplementation.findByName(any()));
    }

    @Test
    void getLecturesByPopularity() {
        when(roleService.findByName(anyString())).thenReturn(roleUser);
        when(userRepository.findAll()).thenReturn(getUsers());

        List<LectureEntity> lectureEntityList = getLecturesEntity();

        when(lectureRepository.findAll()).thenReturn(lectureEntityList);
        List<LectureStatisticsDAO> lectureStatisticsDAOList =
                lectureServiceImplementation.getLecturesByPopularity();

        assertNotNull(lectureStatisticsDAOList);
        assertEquals(lectureEntityList.size(),lectureStatisticsDAOList.size());

    }

    @Test
    void getLecturesByThematicPathPopularity() {
        when(lectureRepository.findAll()).thenReturn(getLecturesEntity());

        List<LectureThematicStatisticDAO> lectureThematicStatisticDAOList =
                lectureServiceImplementation.getLecturesByThematicPathPopularity();

        assertNotNull(lectureThematicStatisticDAOList);
        LectureThematicStatisticDAO lectureThematicStatistic = lectureThematicStatisticDAOList.stream()
                .filter(lecture -> lecture.getThematicPath().equals("Frontend"))
                .findFirst()
                .get();
        assertNotNull(lectureThematicStatistic);
        assertEquals(0, lectureThematicStatistic.getBusySeatsOverAllSeatsTaken());
        assertEquals("Frontend", lectureThematicStatistic.getThematicPath());
        assertEquals(lectureThematicStatisticDAOList.stream()
                .filter(lecture -> lecture.getThematicPath().equals("Frontend"))
                .count(), lectureThematicStatisticDAOList.size());
    }

    private List<LectureEntity> getLecturesEntity() {
        String thematicPathFront = "Frontend";
        return new ArrayList<>(Arrays.asList(
                new LectureEntity("LectureTest1", thematicPathFront, LocalTime.of(10, 0)),
                new LectureEntity("LectureTest2", thematicPathFront, LocalTime.of(12, 0)),
                new LectureEntity("LectureTest3", thematicPathFront, LocalTime.of(14, 0))
        ));
    }

    private List<UserEntity> getUsers() {
        List<UserEntity> userEntityList = new ArrayList<>(Arrays.asList(
                new UserEntity("LoginTest1", "test1@gmail.com", "test1"),
                new UserEntity("LoginTest2", "test2@gmail.com", "test2"),
                new UserEntity("LoginTest3", "test3@gmail.com", "test3")
        ));
        userEntityList.forEach(userEntity -> {
            userEntity.setRoles(new ArrayList<>(Arrays.asList(roleUser)));
        });

        UserEntity user = new UserEntity("LoginTest4", "test4@gmail.com", "test4");
        user.setRoles(new ArrayList<>(Arrays.asList(roleOrganizer)));
        userEntityList.add(user);
        return userEntityList;
    }
}