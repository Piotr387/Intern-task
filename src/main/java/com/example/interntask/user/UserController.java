package com.example.interntask.user;

import com.example.interntask.lecture.dto.LectureDTO;
import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.lecture.dto.LectureStatisticsDAO;
import com.example.interntask.lecture.dto.LectureThematicStatisticDAO;
import com.example.interntask.lecture.service.LectureService;
import com.example.interntask.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    LectureService lectureService;

    @Autowired
    UserService userService;

    /**
     * End point for method GET http://localhost:8080/users
     * @return list of all users stored in database
     */
    @GetMapping()
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    /**
     * End point for method GET http://localhost:8080/users/lectures
     * @return list of all user sign in lectures
     */
    @GetMapping(path = "/lectures")
    @ResponseBody
    public List<LectureDTO> getUsersLectures(@RequestParam(name = "login") String login){
        return lectureService.getLecturesByUserLogin(login);
    }

    /**
     * End point for method POST http://localhost:8080/users/sign-up
     * On this endpoint user will sign up for lecture and
     * if he not exists it will create new user, and send email to him with confirmation
     * @return "SUCCES" or throw exception
     */
    @PostMapping(path = "/sign-up")
    public String signUpUserForLecture(@RequestBody LectureSignUpDTO lectureSignUpDTO){
        return userService.signUp(lectureSignUpDTO);
    }

    /**
     * End point for method DELETE http://localhost:8080/users/{{id}}/cancel/{{lectureId}}
     * On this endpoint user can't cancel his reservation on direct lecture
     * @return "SUCCES" or throw exception
     */
    @DeleteMapping(path = "/{id}/cancel/{lectureId}")
    public String cancelReservation(@PathVariable(name = "id") String userId, @PathVariable(name = "lectureId") String letureId){
        userService.cancelReservation(userId, letureId);
        return "SUCCESS";
    }

    /**
     * End point for method PUT http://localhost:8080/users/{{id}}/update-email
     * On this endpoint user can send request with email update
     * @return "SUCCES" or throw exception
     */
    @PutMapping(path = "/{id}/update-email")
    public String updateEmail(@PathVariable(name = "id") String userId, @RequestBody String newEmail){
        userService.updateEmail(userId,newEmail);
        return "SUCCESS";
    }

    /**
     * End point for method GET http://localhost:8080/users/statistics/lectures-popularity
     * On this endpoint organizator will get list of LectureStatisticsDAO
     * objects more about this object in class
     * @return list of LectureStatisticsDAO objects with statistics for organizer
     *          sorted by busySeatsOverAllUsers then by takenSeat and then by capacity
     */
    @GetMapping(path = "/statistics/lectures-popularity")
    public List<LectureStatisticsDAO> getLecturesByPopularity(){
        return lectureService.getLecturesByPopularity();
    }

    /**
     * End point for method GET http://localhost:8080/users/statistics/thematic-path-popularity
     * On this endpoint organizator will get list of LectureThematicStatisticDAO
     * objects more about this object in class
     * @return list of LectureThematicStatisticDAO objects sorted by busySeatsOverAllSeatsTaken field
     */
    @GetMapping(path = "/statistics/thematic-path-popularity")
    public List<LectureThematicStatisticDAO> getLectures(){
        return lectureService.getLecturesByThematicPathPopularity();
    }
}
