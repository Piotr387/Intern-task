package com.example.interntask.lecture;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.service.LectureService;
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

    /**
     * End point for method GET http://localhost:8080/lectures
     * @return list of all lectures stored in database
     */
    @GetMapping()
    public List<LectureDTO> getLectures(){
        return lectureService.getLectures();
    }
}
