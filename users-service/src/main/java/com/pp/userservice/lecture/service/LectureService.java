package com.pp.userservice.lecture.service;

import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureDetailsDTO;
import com.pp.userservice.lecture.dto.LectureDetailsWithUser;
import com.pp.userservice.lecture.dto.LectureStatisticsDAO;
import com.pp.userservice.lecture.dto.LectureThematicStatisticDAO;

import java.util.List;

/**
 * Interface LectureService it's contains all method that used in program
 * that manipulate data related with lecture. List etc. Because it's an interface
 * we will be able to change database technology without modyfing code.
 * The only thing we will have to do is to add new class that will implements this interface
 * and override method with its own "body" implementation
 */

public interface LectureService {
     List<LectureDetailsWithUser> getLecturesWithUser();
    /**
     * @return all lectures stored in database.
     */
    List<LectureDTO> getLectures();

    /**
     * @param login - user's login
     * @return List of lectures that user with provided login is sign in
     */
    List<LectureDTO> getLecturesByUserLogin(String login);

    /**
     * @return List of lectures with additional statistics data, more info in LectureStatisticsDAO class
     */
    List<LectureStatisticsDAO> getLecturesByPopularity();

    /**
     * @return List of lecture's thematic path with additional statistics,
     * more info in LectureThematicStatisticDAO class
     */
    List<LectureThematicStatisticDAO> getLecturesByThematicPathPopularity();

    LectureEntity findByName(String lectureName);

    List<LectureDetailsDTO> getLecturesDetails();
}
