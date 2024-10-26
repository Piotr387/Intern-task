package com.pp.userservice.lab3.Notification;

abstract class AbstractNotification implements NotificationSender {
    protected String recipient;

    public AbstractNotification(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public abstract String sendNotification(String message);
}
