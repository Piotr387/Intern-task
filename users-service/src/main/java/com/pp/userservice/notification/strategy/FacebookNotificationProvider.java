package com.pp.userservice.notification.strategy;

import com.pp.userservice.notification.decorator.FacebookNotifier;
import com.pp.userservice.notification.decorator.Notifier;
import com.pp.userservice.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class FacebookNotificationProvider implements NotificationProvider {
    @Override
    public boolean appliesTo(UserEntity userEntity) {
        return userEntity.isFacebookNotification();
    }

    @Override
    public Notifier extendNotifier(Notifier notifier) {
        return new FacebookNotifier(notifier);
    }
}
