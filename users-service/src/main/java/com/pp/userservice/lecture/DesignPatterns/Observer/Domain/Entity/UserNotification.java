package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Observer.Observer;
import com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure.NotificationRepository;

public class UserNotification implements Observer {
    Integer id;

    public UserNotification(Integer id) {
        this.id = id;
    }

    @Override
    public void update(String string, NotificationRepository notificationRepository) {
        notificationRepository.save(new NotificationEntity(id, string));
    }
}
