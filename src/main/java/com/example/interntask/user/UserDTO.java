package com.example.interntask.user;

import com.example.interntask.lecture.dto.LectureDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Class responsible for data transfer object most of the time from UserEntity or
 * from UserDto to userEntity
 * Objects of this type are return or accept as a @RequestBody
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String login;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LectureDTO> lectureDTOList;

    public UserDTO(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
