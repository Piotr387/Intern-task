package com.pp.userservice.notification.decorator;

import lombok.extern.slf4j.Slf4j;
// start L1 Decorator
@Slf4j
public class SMSNotifier extends NotifierDecorator {
    public SMSNotifier(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send(String message) {
        log.info("Sending SMS message");
        super.send(message);
    }
}
