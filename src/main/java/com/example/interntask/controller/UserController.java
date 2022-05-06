package com.example.interntask.controller;

import com.example.interntask.DTO.UserDTO;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

}
