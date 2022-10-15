package com.pp.lecture.dto;

import com.pp.lecture.LectureEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * Class is used in one case it's a return type on url below
 * GET http://localhost:8080/users/statistics/lectures-popularity
 * Main goal of this class is to nicely represent statistics about each lecture
 * it contains data about lecture and also information about seat capacity on lecture and seatTaken
 * Last field is busySeatsOverAllUsers which is a number expressed in percentage
 * It's taking seatTaken and it's divided by number of all register user.
 * in future, it will take number of user with role "USER"
 */

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
        if (lectureEntity.getUserEntityListSize() != 0 && numberOfUsers != 0) {
            this.seatTaken = lectureEntity.getUserEntityListSize();
            busySeatsOverAllUsers = setPrecisionTwoAfterComma(Double.valueOf(seatTaken) / numberOfUsers * 100);
        } else {
            this.seatTaken = 0;
            this.busySeatsOverAllUsers = 0;
        }
    }

    private double setPrecisionTwoAfterComma(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
