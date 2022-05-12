package com.example.interntask.user;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    LectureService lectureService;

    @Autowired
    UserService userService;


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
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/lectures
     * @return list of all user's sign in lectures
     */
    @GetMapping(path = "/lectures")
    @ResponseBody
    public List<LectureDTO> getUsersLectures(HttpServletRequest request) {
        return userService.getUserLecture(request);
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/account
     * @return user account details
     */
    @GetMapping(path = "/account")
    @ResponseBody
    public UserDTO getUserDetails(HttpServletRequest request) {
        return userService.getUserAccountDetails(request);
    }

    /**
     * Endpoint for method POST http://localhost:8080/users/sign-up
     * On this endpoint user will sign up for lecture and
     * if he not exists it will create new user, and send email to him with confirmation
     *
     * @return "SUCCES" or throw exception
     */
    @PostMapping(path = "/sign-up")
    public String signUpUserForLecture(@RequestBody LectureSignUpDTO lectureSignUpDTO) {
        return userService.signUp(lectureSignUpDTO);
    }

    /**
     * Endpoint for method POST http://localhost:8080/users/sign-up-register
     * On this endpoint registered user will be able to sign up for lecture
     * it will secure registered user from being sign up by other users' without authorization'
     * @return "SUCCESS" or throw exception
     */
    @PostMapping(path = "/sign-up-register")
    public String signUpRegisterUserForLecture(HttpServletRequest request) {
        return userService.signUpRegister(request);
    }

    /**
     * Endpoint for method DELETE http://localhost:8080/users/{{id}}/cancel/{{lectureId}}
     * On this endpoint user can't cancel his reservation on direct lecture
     *
     * @return "SUCCESS" or throw exception
     */
    @DeleteMapping(path = "/cancel")
    public String cancelReservation(HttpServletRequest request) {
        userService.cancelReservation(request);
        return "SUCCESS";
    }

    /**
     * Endpoint for method PUT http://localhost:8080/users/{{id}}/update-email
     * On this endpoint user can send request with email update
     *
     * @return "SUCCESS" or throw exception
     */
    @PutMapping(path = "/update-email")
    public String updateEmail(HttpServletRequest request) {
        userService.updateEmail(request);
        return "SUCCESS";
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
    public List<LectureStatisticsDAO> getLecturesByPopularity() {
        return lectureService.getLecturesByPopularity();
    }

    /**
     * Endpoint for method GET http://localhost:8080/users/statistics/thematic-path-popularity
     * On this endpoint organizer will get list of LectureThematicStatisticDAO
     * objects more about this object in class
     *
     * @return list of LectureThematicStatisticDAO objects sorted by busySeatsOverAllSeatsTaken field
     */
    @GetMapping(path = "/statistics/thematic-path-popularity")
    public List<LectureThematicStatisticDAO> getLectures() {
        return lectureService.getLecturesByThematicPathPopularity();
    }
}
