package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Service;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.NotificationEntity;

import java.util.List;

public interface NotificationsProviderInterface {
     List<NotificationEntity> getNotifications();
}
