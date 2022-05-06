package com.example.interntask.controller;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.entity.LectureEntity;
import com.example.interntask.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {

    @Autowired
    LectureService lectureService;

    @GetMapping()
    public List<LectureDTO> getLectures(){
        return lectureService.getLectures();
    }
}
