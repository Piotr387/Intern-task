package com.example.interntask.controller;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.DTO.UserDTO;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.service.LectureService;
import com.example.interntask.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping(path = "/sign-up/{lectureName}")
    public String signUpUserForLecture(@PathVariable String lectureName,@RequestBody UserDTO userDTO){
        return userService.signUp(lectureName,userDTO);
    }

}
