package com.pp.userservice.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDetailsDTO {
    private LectureDTO lectureDTO;
    private int capacity;
    private int takenSeats;
}
