package com.example.interntask.controller;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.DTO.LectureSignUpDTO;
import com.example.interntask.DTO.UserDTO;
import com.example.interntask.DTO.statistics.LectureStatisticsDAO;
import com.example.interntask.DTO.statistics.LectureThematicStatisticDAO;
import com.example.interntask.service.LectureService;
import com.example.interntask.service.UserService;
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

    @GetMapping()
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path = "/lectures")
    @ResponseBody
    public List<LectureDTO> getUsersLectures(@RequestParam(name = "login") String login){
        return lectureService.getLecturesByUserLogin(login);
    }

    @PostMapping(path = "/sign-up")
    public String signUpUserForLecture(@RequestBody LectureSignUpDTO lectureSignUpDTO){
        return userService.signUp(lectureSignUpDTO);
    }

    @DeleteMapping(path = "/{id}/cancel/{lectureId}")
    public String cancelReservation(@PathVariable(name = "id") String userId, @PathVariable(name = "lectureId") String letureId){
        userService.cancelReservation(userId, letureId);
        return "SUCCESS";
    }

    @PutMapping(path = "/{id}/update-email")
    public String updateEmail(@PathVariable(name = "id") String userId, @RequestBody String newEmail){
        userService.updateEmail(userId,newEmail);
        return "SUCCESS";
    }

    @GetMapping(path = "/statistics/lectures-popularity")
    public List<LectureStatisticsDAO> getLecturesByPopularity(){
        return lectureService.getLecturesByPopularity();
    }

    @GetMapping(path = "/statistics/thematic-path-popularity")
    public List<LectureThematicStatisticDAO> getLectures(){
        return lectureService.getLecturesByThematicPathPopularity();
    }
}
