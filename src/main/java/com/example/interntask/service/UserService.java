package com.example.interntask.service;

import com.example.interntask.DTO.UserDTO;
import com.example.interntask.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getUsers();
}
