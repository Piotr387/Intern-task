package com.pp.userservice.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class is used in one case it's a return type on url below
 * GET http://localhost:8080/users/statistics/thematic-path-popularity
 * Main goal of this class is to nicely represent statistics about lecture's thematic path
 * it contains data about lecture thematic name, all takenSeats expressed in percentage
 * it takes all busy seats in each thematic path and it's divided by all busy seats
 * (sum of all busy seats in each category, contains user duplication on different hours)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureThematicStatisticDAO {
    private String thematicPath;
    /**
     * Percentage value calculated from the Busy seats in category over all seats taken in the event
     */
    private Double busySeatsOverAllSeatsTaken;

    public LectureThematicStatisticDAO(String thematicPath, Double busySeatInCategory,int  allBusySeats) {
        this.thematicPath = thematicPath;
        if (busySeatInCategory != 0 && allBusySeats != 0){
            this.busySeatsOverAllSeatsTaken = setPrecisionTwoAfterComma(busySeatInCategory/allBusySeats*100);
        } else {
            this.busySeatsOverAllSeatsTaken = 0D;
        }

    }

    private double setPrecisionTwoAfterComma(double number){
        return Math.round(number*100.0)/100.0;
    }
}
