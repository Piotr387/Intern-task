package com.pp.userservice.notification.strategy;

import com.pp.userservice.notification.decorator.Notifier;
import com.pp.userservice.notification.decorator.SMSNotifier;
import com.pp.userservice.user.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class SMSNotificationProvider implements NotificationProvider {
    @Override
    public boolean appliesTo(UserEntity userEntity) {
        return userEntity.isSMSNotification();
    }

    @Override
    public Notifier extendNotifier(Notifier notifier) {
        return new SMSNotifier(notifier);
    }
}
