package com.pp.userservice.lab3.SingleAbstractionLayer.CorrectedExamples;

import com.pp.userservice.lab3.Notification.NotificationSender;

public class UserAccountManager {
    private final NotificationSender notificationSender;

    public UserAccountManager(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    public void createUserAccount(String userName, String password) {
        String encryptedPassword = encryptPassword(password);
        saveUserToDatabase(userName, encryptedPassword);
        notifyUser(userName);
    }

    private String encryptPassword(String password) {
        return "encrypted" + password;
    }

    private void saveUserToDatabase(String userName, String encryptedPassword) {
        System.out.println("Saving " + userName + " to the database with password: " + encryptedPassword);
    }

    private void notifyUser(String userName) {
        notificationSender.sendNotification("Welcome, " + userName + "!");
    }
}
