package com.pp.userservice.lab3.SingleResponsibility.WrongExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class UserManager {
    private final NotificationSender notificationSender;

    public UserManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void registerUserAndNotify(String userName, String email) {
        System.out.println("Registering user: " + userName);
        notificationSender.sendNotification("Welcome " + userName + "! You have successfully registered.");
    }
}
