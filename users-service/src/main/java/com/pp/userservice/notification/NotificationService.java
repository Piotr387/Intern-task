package com.pp.userservice.notification;

import com.pp.userservice.notification.decorator.DefaultNotifier;
import com.pp.userservice.notification.decorator.Notifier;
import com.pp.userservice.notification.strategy.NotificationProvider;
import com.pp.userservice.user.entity.UserEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// start L1 Facade - we just need to call this service to send notification it will automatically configure which notification have to be sent.
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationProvider> notificationProviders;

    public void sendNotification(UserEntity userEntity, String message) {

        Notifier notifier = new DefaultNotifier();

        // start L2 Strategy
        for (NotificationProvider provider : notificationProviders) {
            if (provider.appliesTo(userEntity)) {
                notifier = provider.extendNotifier(notifier);
            }
        }

        notifier.send(message);
    }
}
