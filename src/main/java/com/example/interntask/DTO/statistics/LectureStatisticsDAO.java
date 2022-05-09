package com.example.interntask.DTO.statistics;

import com.example.interntask.DTO.LectureDTO;
import com.example.interntask.entity.LectureEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureStatisticsDAO {
    private LectureDTO lectureDTO;
    private int seatCapacity;
    private int seatTaken;
    /**
     * Percentage value calculated from the Busy seats over all users that are register in system
     * After adding a roles it will need to be updated
     */
    private double busySeatsOverAllUsers;

    public LectureStatisticsDAO(LectureEntity lectureEntity, long numberOfUsers) {
        this.lectureDTO = new ModelMapper().map(lectureEntity, LectureDTO.class);
        this.seatCapacity = lectureEntity.getCAPACITY();
        if (lectureEntity.getUserEntityListSize() != 0 && numberOfUsers != 0){
            this.seatTaken = lectureEntity.getUserEntityListSize();
            busySeatsOverAllUsers = setPrecisionTwoAfterComma( Double.valueOf(seatTaken)/numberOfUsers*100);
        } else {
            this.seatTaken = 0;
            this.busySeatsOverAllUsers = 0;
        }
    }

    private double setPrecisionTwoAfterComma(double number){
        return Math.round(number*100.0)/100.0;
    }
}
