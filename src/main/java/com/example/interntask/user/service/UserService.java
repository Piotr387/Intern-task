package com.example.interntask.user.service;

import com.example.interntask.lecture.dto.LectureSignUpDTO;
import com.example.interntask.user.UserDTO;

import java.util.List;

/**
 * Interface UserService it's contains all method that used in program
 *  * that manipulate data related with users. Because it's an interface
 *  * we will be able to change database technology without modyfing code.
 *  The only thing we will have to do is to add new class that will implements this interface
 *  and override method with its own "body" implementation
 */

public interface UserService {
    List<UserDTO> getUsers();
    String signUp(LectureSignUpDTO lectureSignUpDTO);
    void cancelReservation(String userId, String letureId);

    void updateEmail(String userId, String newEmail);
}
