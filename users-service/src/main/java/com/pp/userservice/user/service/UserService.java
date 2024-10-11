package com.pp.userservice.user.service;

import com.pp.userservice.lecture.dto.LectureDTO;
import com.pp.userservice.lecture.dto.LectureSignUpDTO;
import com.pp.userservice.lecture.dto.LectureWithFirstRegistration;
import com.pp.userservice.response.OperationStatusModel;
import com.pp.userservice.user.UserDTO;
import com.pp.userservice.user.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Interface UserService it's contains all method that used in program
 * * that manipulate data related with users. Because it's an interface
 * * we will be able to change database technology without modyfing code.
 * The only thing we will have to do is to add new class that will implements this interface
 * and override method with its own "body" implementation
 */
// start L1 bridge
public interface UserService {
    List<UserDTO> getUsers();

    UserEntity createUser(UserDTO userDTO);

    UserEntity createUserWithPassword(UserDTO userDTO, String password);

    OperationStatusModel signUp(LectureSignUpDTO lectureSignUpDTO);

    OperationStatusModel signUpRegister(HttpServletRequest request);

    OperationStatusModel signUpForLecture(UserEntity userEntity, String lectureName);

    OperationStatusModel cancelReservation(HttpServletRequest request);

    void updateEmail(HttpServletRequest request);

    void addRoleToUser(UserEntity userEntity, String roleName);

    UserEntity getUser(String login);

    void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    List<LectureDTO> getUserLecture(HttpServletRequest request);

    UserDTO getUserAccountDetails(HttpServletRequest request);

    OperationStatusModel signUp(LectureWithFirstRegistration lectureSignUpDTO);
}
