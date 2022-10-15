package com.pp.lecture;

import com.pp.lecture.dto.LectureDTO;
import com.pp.lecture.dto.LectureDetailsDTO;
import com.pp.lecture.service.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pp.lecture.LectureController.LECTURES_ENDPOINT;

@RestController
@RequestMapping(value = LECTURES_ENDPOINT)
@AllArgsConstructor
class LectureController {

    static final String LECTURES_ENDPOINT = "/lectures";
    private final LectureService lectureService;

    /**
     * End point for method GET http://localhost:8080/lectures
     *
     * @return list of all lectures stored in database
     */
    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureDTO> getLectures() {
        return lectureService.getLectures();
    }

    @GetMapping(path = "/details")
    @ResponseStatus(value = HttpStatus.OK)
    List<LectureDetailsDTO> getLecturesDetails() {
        return lectureService.getLecturesDetails();
    }
}
