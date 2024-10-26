package com.pp.userservice.lab3.Reservation;

public class LectureReservation extends AbstractReservation{
    public LectureReservation(String reservationId) {
        super(reservationId);
    }

    @Override
    public String reserveSeat(String user, String lectureTitle) {
        return "Reserved seat for " + user + " in lecture: " + lectureTitle + " with reservation ID: " + reservationId;
    }
}
