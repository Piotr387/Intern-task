package com.pp.userservice.lab3.SingleResponsibility.CorectedExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class UserManager {
    private final NotificationSender notificationSender;

    public UserManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void registerUser(String userName) {
        System.out.println("Registering user: " + userName);
    }

    public void sendWelcomeNotification(String userName) {
        notificationSender.sendNotification("Welcome " + userName + "! You have successfully registered.");
    }

    public void registerUserAndNotify(String userName, String email) {
        registerUser(userName);
        sendWelcomeNotification(userName);
    }
}
