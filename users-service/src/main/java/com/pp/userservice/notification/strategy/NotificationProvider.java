package com.pp.userservice.notification.strategy;

import com.pp.userservice.notification.decorator.Notifier;
import com.pp.userservice.user.entity.UserEntity;

// start L2 Strategy
public interface NotificationProvider {
    boolean appliesTo(UserEntity userEntity);

    Notifier extendNotifier(Notifier notifier);
}

