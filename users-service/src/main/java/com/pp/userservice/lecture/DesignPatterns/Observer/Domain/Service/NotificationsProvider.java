package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Service;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.NotificationEntity;
import com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationsProvider implements NotificationsProviderInterface {
    private NotificationRepository notificationRepository;

    @Override
    public List<NotificationEntity> getNotifications() {
        return notificationRepository.findAll().stream().toList();
    }
}
