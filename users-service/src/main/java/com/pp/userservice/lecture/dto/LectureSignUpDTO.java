package com.pp.userservice.lecture.dto;

import com.pp.userservice.user.api.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class is used in one case to sign up user for the lecture
 * It's used as a @RequestBody
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureSignUpDTO {
    private String lectureName;
    private UserDTO userDTO;
}
