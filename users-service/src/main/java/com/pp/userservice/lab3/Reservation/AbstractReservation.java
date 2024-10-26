package com.pp.userservice.lab3.Reservation;

abstract class AbstractReservation implements ReservationSystem {
    protected String reservationId;

    public AbstractReservation(String reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public abstract String reserveSeat(String user, String lectureTitle);
}
