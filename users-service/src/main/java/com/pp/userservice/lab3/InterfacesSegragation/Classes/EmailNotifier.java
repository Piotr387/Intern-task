package com.pp.userservice.lab3.InterfacesSegragation.Classes;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Notification.EmailNotificationInterface;

public class EmailNotifier implements EmailNotificationInterface {
    @Override
    public String sendEmail(String recipient, String message) {
        return "Sending email to " + recipient + ": " + message;
    }
}
