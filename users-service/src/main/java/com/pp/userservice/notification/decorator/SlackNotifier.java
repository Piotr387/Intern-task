package com.pp.userservice.notification.decorator;

import lombok.extern.slf4j.Slf4j;
// start L1 Decorator
@Slf4j
public class SlackNotifier extends NotifierDecorator{
    public SlackNotifier(Notifier notifier) {
        super(notifier);
    }

    @Override
    public void send(String message) {
        log.info("Sending Notification to Slack application with content {}", message);
        super.send(message);
    }
}
