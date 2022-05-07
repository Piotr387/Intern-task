package com.example.interntask.service;

import com.example.interntask.DTO.LectureSignUpDTO;
import com.example.interntask.DTO.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers();
    String signUp(LectureSignUpDTO lectureSignUpDTO);
    void cancelReservation(String userId, String letureId);
}
