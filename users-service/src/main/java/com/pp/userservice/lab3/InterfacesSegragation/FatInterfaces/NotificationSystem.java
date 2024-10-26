package com.pp.userservice.lab3.InterfacesSegragation.FatInterfaces;

public interface NotificationSystem {
    void sendEmail(String recipient, String message);
    void sendSMS(String recipient, String message);
    void sendPushNotification(String recipient, String message);
    void scheduleNotification(String recipient, String message, String date);
}
