package com.pp.userservice.notification.decorator;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// start L1 Decorator
@Slf4j
@NoArgsConstructor
public class DefaultNotifier implements Notifier {

    @Override
    public void send(String message) {
        log.info("Message was successfully send!");
        log.debug("Message with body send: {}", message);
    }
}
