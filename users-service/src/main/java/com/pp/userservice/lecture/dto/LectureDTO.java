package com.pp.userservice.lecture.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pp.userservice.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 * Class responsible for data transfer object most of the time from LectureEntity
 * Objects of this type are return or accept as a @RequestBody
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    private String name;
    private String thematicPath;
    private LocalTime startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserDTO> userDTOList;
}
