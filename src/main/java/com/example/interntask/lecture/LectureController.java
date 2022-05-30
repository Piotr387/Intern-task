package com.example.interntask.lecture;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureDetailsDTO;
import com.example.interntask.lecture.service.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lectures")
@AllArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * End point for method GET http://localhost:8080/lectures
     * @return list of all lectures stored in database
     */
    @GetMapping()
    public ResponseEntity<List<LectureDTO>> getLectures(){
        return new ResponseEntity<>(lectureService.getLectures(), HttpStatus.OK);
    }

    @GetMapping(path = "/details")
    public ResponseEntity<List<LectureDetailsDTO>> getLecturesDetails(){
        return new ResponseEntity<>(lectureService.getLecturesDetails(), HttpStatus.OK);
    }
}
