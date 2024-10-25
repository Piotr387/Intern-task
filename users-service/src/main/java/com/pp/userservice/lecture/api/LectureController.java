package com.pp.userservice.lecture.api;

import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureDetailsDTO;
import com.pp.userservice.lecture.dto.LectureDetailsWithUser;
import com.pp.userservice.lecture.dto.LectureStatisticsDAO;
import com.pp.userservice.lecture.dto.LectureThematicStatisticDAO;
import com.pp.userservice.lecture.service.LectureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pp.userservice.lecture.api.LectureController.LECTURES_ENDPOINT;

@RestController
@RequestMapping(value = LECTURES_ENDPOINT)
@AllArgsConstructor
class LectureController {

  static final String LECTURES_ENDPOINT = "/lectures";
  private final LectureService lectureService;

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

  /**
   * Endpoint for method GET http://localhost:8080/users/statistics/lectures-popularity On this
   * endpoint organizer will get list of LectureStatisticsDAO objects more about this object in
   * class
   *
   * @return list of LectureStatisticsDAO objects with statistics for organizer sorted by
   * busySeatsOverAllUsers then by takenSeat and then by capacity
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/statistics/lectures-popularity")
  ResponseEntity<List<LectureStatisticsDAO>> getLecturesByPopularity() {
    return new ResponseEntity<>(lectureService.getLecturesByPopularity(), HttpStatus.OK);
  }

  /**
   * Endpoint for method GET http://localhost:8080/users/statistics/thematic-path-popularity On this
   * endpoint organizer will get list of LectureThematicStatisticDAO objects more about this object
   * in class
   *
   * @return list of LectureThematicStatisticDAO objects sorted by busySeatsOverAllSeatsTaken field
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/statistics/thematic-path-popularity")
  List<LectureThematicStatisticDAO> getLecturesByThematicPathPopularity() {

    return lectureService.getLecturesByThematicPathPopularity();
  }

  @GetMapping("/organizer/details")
  @ResponseStatus(HttpStatus.OK)
  public List<LectureDetailsWithUser> getLectureOrganizerDetails(){

    return lectureService.getLecturesWithUser();
  }
}
