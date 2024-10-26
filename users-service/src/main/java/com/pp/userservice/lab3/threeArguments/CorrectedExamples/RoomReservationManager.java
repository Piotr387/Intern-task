package com.pp.userservice.lab3.threeArguments.CorrectedExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class RoomReservationManager {
    private final NotificationSender notificationSender;

    public RoomReservationManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void reserveRoomForLecture(ReservationDetails details) {
        if (isRoomAvailable(details)) {
            saveReservation(details);
            notifyReservation(details);
        } else {
            System.out.println("Room not available for reservation.");
        }
    }

    private boolean isRoomAvailable(ReservationDetails details) {
        return checkRoomAvailability(details.room, details.attendees);
    }

    private boolean checkRoomAvailability(String room, int attendees) {
        return attendees <= 50;
    }

    private void saveReservation(ReservationDetails details) {
        saveReservationToDatabase(details.room, details.lectureTitle, details.date, details.attendees);
    }

    private void saveReservationToDatabase(String room, String lectureTitle, String date, int attendees) {
        System.out.println("Saving reservation for " + lectureTitle + " in room " + room + " on " + date + " for " + attendees + " attendees.");
    }

    private void notifyReservation(ReservationDetails details) {
        sendReservationNotification(details.room, details.lectureTitle);
    }

    private void sendReservationNotification(String room, String lectureTitle) {
        notificationSender.sendNotification("Room " + room + " has been reserved for lecture: " + lectureTitle);
    }
}
