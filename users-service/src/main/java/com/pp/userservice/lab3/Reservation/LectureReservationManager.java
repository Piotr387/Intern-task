package com.pp.userservice.lab3.Reservation;

import com.pp.userservice.lab3.Notification.NotificationSender;
import com.pp.userservice.lab3.Payment.PaymentProcessor;

import java.util.ArrayList;
import java.util.List;

public class LectureReservationManager {
    private final ReservationSystem reservationSystem;
    private final PaymentProcessor paymentProcessor;
    private final NotificationSender notificationSender;

    public LectureReservationManager(ReservationSystem reservationSystem, PaymentProcessor paymentProcessor, NotificationSender notificationSender) {
        this.reservationSystem = reservationSystem;
        this.paymentProcessor = paymentProcessor;
        this.notificationSender = notificationSender;
    }

    public List<String> reserveAndNotify(String user, String lectureTitle, double paymentAmount) {
        List<String> result = new ArrayList<>();
        result.add(reservationSystem.reserveSeat(user, lectureTitle));
        result.add(paymentProcessor.processPayment(paymentAmount));
        result.add(notificationSender.sendNotification("Reservation confirmed for " + lectureTitle + "!"));

        return result;
    }
}
