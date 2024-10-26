package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Observer;

import com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure.NotificationRepository;

public interface Observer {
    void update(String string, NotificationRepository notificationRepository);
}
