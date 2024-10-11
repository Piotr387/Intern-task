package com.pp.userservice.notification.decorator;

import lombok.RequiredArgsConstructor;
// start L1 Decorator
@RequiredArgsConstructor
public class NotifierDecorator implements Notifier {

    private final Notifier notifier;

    @Override
    public void send(String message) {
        notifier.send(message);
    }
}
