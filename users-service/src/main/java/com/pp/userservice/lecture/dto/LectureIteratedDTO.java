package com.pp.userservice.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureIteratedDTO {
    private int counter;
    private String name;
    private String thematicPath;
    private LocalTime startTime;
}
