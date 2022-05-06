package com.example.interntask.service;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.entity.LectureEntity;

import java.util.List;

public interface LectureService {
    List<LectureDTO> getLectures();
}
