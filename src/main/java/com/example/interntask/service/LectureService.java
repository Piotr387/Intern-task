package com.example.interntask.service;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.DTO.statistics.LectureStatisticsDAO;
import com.example.interntask.DTO.statistics.LectureThematicStatisticDAO;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getLectures();
    List<LectureDTO> getLecturesByUserLogin(String login);

    List<LectureStatisticsDAO> getLecturesByPopularity();

    List<LectureThematicStatisticDAO> getLecturesByThematicPathPopularity();
}
