package com.pp.userservice.notification.strategy;

import com.pp.userservice.notification.decorator.Notifier;
import com.pp.userservice.notification.decorator.SlackNotifier;
import com.pp.userservice.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class SlackNotificationProvider implements NotificationProvider {
    @Override
    public boolean appliesTo(UserEntity userEntity) {
        return userEntity.isSlackNotification();
    }

    @Override
    public Notifier extendNotifier(Notifier notifier) {
        return new SlackNotifier(notifier);
    }
}
