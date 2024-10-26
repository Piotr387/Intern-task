package com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Notification;

public interface SMSNotification {
    String sendSMS(String recipient, String message);
}
