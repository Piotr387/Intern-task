package com.example.interntask.user;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.responde.OperationStatusModel;
import com.example.interntask.responde.RequestOperationName;
import com.example.interntask.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final LectureService lectureService;
    private final UserService userService;

    /**
     * Endpoint for method GET http://localhost:8080/users/token/refresh
     * When the user_token will be expired, we will be able to get another token without
     * sign in procedure
     */
    @GetMapping(path = "/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.getRefreshToken(request, response);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users
     * @return list of all users stored in database
     */
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/lectures
     * @return list of all user's sign in lectures
     */
    @GetMapping(path = "/lectures")
    @ResponseBody
    public ResponseEntity<List<LectureDTO>> getUsersLectures(HttpServletRequest request) {
        return new ResponseEntity<>( userService.getUserLecture(request), HttpStatus.OK);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/account
     * @return user account details
     */
    @GetMapping(path = "/account")
    @ResponseBody
    public ResponseEntity<UserDTO> getUserDetails(HttpServletRequest request) {
        return new ResponseEntity<>(userService.getUserAccountDetails(request), HttpStatus.OK);
    }

    /**
     * Endpoint for method POST http://localhost:8080/users/sign-up
     * On this endpoint user will sign up for lecture and
     * if he not exists it will create new user, and send email to him with confirmation
     *
     * @return "SUCCES" or throw exception
     */
    @PostMapping(path = "/sign-up")
    public ResponseEntity<OperationStatusModel> signUpUserForLecture(@RequestBody LectureSignUpDTO lectureSignUpDTO) {
        return new ResponseEntity<>(userService.signUp(lectureSignUpDTO), HttpStatus.OK);
    }

    /**
     * Endpoint for method POST http://localhost:8080/users/sign-up-register
     * On this endpoint registered user will be able to sign up for lecture
     * it will secure registered user from being sign up by other users' without authorization'
     * @return "SUCCESS" or throw exception
     */
    @PostMapping(path = "/sign-up-register")
    public ResponseEntity<OperationStatusModel> signUpRegisterUserForLecture(HttpServletRequest request) {
        return new ResponseEntity<>(userService.signUpRegister(request), HttpStatus.OK);
    }

    /**
     * Endpoint for method DELETE http://localhost:8080/users/{{id}}/cancel/{{lectureId}}
     * On this endpoint user can't cancel his reservation on direct lecture
     *
     * @return "SUCCESS" or throw exception
     */
    @DeleteMapping(path = "/cancel")
    public ResponseEntity<OperationStatusModel> cancelReservation(HttpServletRequest request) {
        return new ResponseEntity<>(userService.cancelReservation(request), HttpStatus.OK);
    }

    /**
     * Endpoint for method PUT http://localhost:8080/users/{{id}}/update-email
     * On this endpoint user can send request with email update
     *
     * @return "SUCCESS" or throw exception
     */
    @PutMapping(path = "/update-email")
    public ResponseEntity<OperationStatusModel> updateEmail(HttpServletRequest request) {
        userService.updateEmail(request);
        return new ResponseEntity<>(new OperationStatusModel.Builder(RequestOperationName.CHANGE_EMAIL.name()).build(), HttpStatus.OK);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/statistics/lectures-popularity
     * On this endpoint organizer will get list of LectureStatisticsDAO
     * objects more about this object in class
     *
     * @return list of LectureStatisticsDAO objects with statistics for organizer
     * sorted by busySeatsOverAllUsers then by takenSeat and then by capacity
     */
    @GetMapping(path = "/statistics/lectures-popularity")
    public ResponseEntity<List<LectureStatisticsDAO>> getLecturesByPopularity() {
        return new ResponseEntity<>(lectureService.getLecturesByPopularity(), HttpStatus.OK);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/statistics/thematic-path-popularity
     * On this endpoint organizer will get list of LectureThematicStatisticDAO
     * objects more about this object in class
     *
     * @return list of LectureThematicStatisticDAO objects sorted by busySeatsOverAllSeatsTaken field
     */
    @GetMapping(path = "/statistics/thematic-path-popularity")
    public ResponseEntity<List<LectureThematicStatisticDAO>> getLectures() {
        return new ResponseEntity<>(lectureService.getLecturesByThematicPathPopularity(), HttpStatus.OK);
    }
}
