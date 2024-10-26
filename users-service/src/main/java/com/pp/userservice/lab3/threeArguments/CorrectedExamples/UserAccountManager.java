package com.pp.userservice.lab3.threeArguments.CorrectedExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class UserAccountManager {
    private final NotificationSender notificationSender;

    public UserAccountManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void createUserAccount(UserData userData) {
        String encryptedPassword = encryptPassword(userData.password);
        saveUserToDatabase(userData.userName, encryptedPassword, userData.email, userData.phoneNumber);
        sendWelcomeNotification(userData.userName, userData.email);
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
