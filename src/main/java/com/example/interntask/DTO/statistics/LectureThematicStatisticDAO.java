package com.example.interntask.DTO.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
