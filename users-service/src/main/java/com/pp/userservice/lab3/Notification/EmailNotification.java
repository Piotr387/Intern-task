package com.pp.userservice.lab3.Notification;

public class EmailNotification extends AbstractNotification {
    public EmailNotification(String recipient) {
        super(recipient);
    }

    @Override
    public String sendNotification(String message) {
        return "Sending email to " + recipient + ": " + message;
    }
}
