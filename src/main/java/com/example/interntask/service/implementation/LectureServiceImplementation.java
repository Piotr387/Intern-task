package com.example.interntask.service.implementation;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.entity.LectureEntity;
import com.example.interntask.repositories.LectureRepository;
import com.example.interntask.service.LectureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LectureServiceImplementation implements LectureService {

    @Autowired
    LectureRepository lectureRepository;

    @Override
    public List<LectureDTO> getLectures() {
        return lectureRepository.findAll().stream()
                .map(entity -> new ModelMapper().map(entity, LectureDTO.class))
                .collect(Collectors.toList());
    }
}
