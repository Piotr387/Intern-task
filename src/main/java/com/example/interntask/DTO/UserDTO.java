package com.example.interntask.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String login;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<LectureDTO> lectureDTOList;
}
