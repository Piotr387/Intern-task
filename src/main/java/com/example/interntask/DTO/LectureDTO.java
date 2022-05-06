package com.example.interntask.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    private String name;
    private LocalTime startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDTO> userDTOList;
}
