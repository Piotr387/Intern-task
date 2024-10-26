package com.pp.userservice.lab3.threeArguments.WrongExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class RoomReservationManager {

    //l3z10
    private static final int MAX_ATTENDANTS = 5;

    private final NotificationSender notificationSender;

    public RoomReservationManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void reserveRoomForLecture(String room, String lectureTitle, String date, int attendees) {
        if (checkRoomAvailability(room, attendees)) {
            saveReservationToDatabase(room, lectureTitle, date, attendees);
            sendReservationNotification(room, lectureTitle);
        } else {
            System.out.println("Room not available for reservation.");
        }
    }

    private boolean checkRoomAvailability(String room, int attendees) {
        return attendees <= MAX_ATTENDANTS;
    }

    private void saveReservationToDatabase(String room, String lectureTitle, String date, int attendees) {
        System.out.println("Saving reservation for " + lectureTitle + " in room " + room + " on " + date + " for " + attendees + " attendees.");
    }

    private void sendReservationNotification(String room, String lectureTitle) {
        notificationSender.sendNotification("Room " + room + " has been reserved for lecture: " + lectureTitle);
    }
}
