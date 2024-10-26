package com.pp.userservice.lab3.threeArguments.WrongExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class UserAccountManager {
    private final NotificationSender notificationSender;

    public UserAccountManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void createUserAccount(String userName, String password, String email, String phoneNumber) {
        String encryptedPassword = encryptPassword(password);
        saveUserToDatabase(userName, encryptedPassword, email, phoneNumber);
        sendWelcomeNotification(userName, email);
    }

    private String encryptPassword(String password) {
        return "encrypted" + password;
    }

    private void saveUserToDatabase(String userName, String encryptedPassword, String email, String phoneNumber) {
        System.out.println("Saving " + userName + " with email " + email + " and phone " + phoneNumber + " to the database.");
    }

    private void sendWelcomeNotification(String userName, String email) {
        notificationSender.sendNotification("Welcome, " + userName + "! Confirmation sent to " + email);
    }
}
