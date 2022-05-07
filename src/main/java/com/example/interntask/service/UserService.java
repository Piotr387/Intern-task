package com.example.interntask.service;

import com.example.interntask.DTO.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();
    String signUp(String lectureName, UserDTO userDTO);
}
