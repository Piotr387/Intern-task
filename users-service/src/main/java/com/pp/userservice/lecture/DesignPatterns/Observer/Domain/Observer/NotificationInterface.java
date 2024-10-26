package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Observer;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.UserNotification;

import java.util.List;

public interface NotificationInterface {
    void registerObserver(Integer userId);
    void removeObserver(UserNotification user);
    void notifyObservers();
}
