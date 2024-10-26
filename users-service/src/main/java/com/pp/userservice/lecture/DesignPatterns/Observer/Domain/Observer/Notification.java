package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Observer;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.UserNotification;
import com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure.NotificationRepository;

import java.util.List;

public class Notification implements NotificationInterface {
    private final NotificationRepository repository;
    private List<UserNotification> observers;
    private final String message;

    public Notification(NotificationRepository repository, List<UserNotification> observers, String message)
    {
        this.repository = repository;
        this.observers = observers;
        this.message = message;
    }

    @Override
    public void registerObserver(Integer userId) {
        observers.add(new UserNotification(userId));
    }

    @Override
    public void removeObserver(UserNotification user) {
        observers.remove(user);
    }

    @Override
    public void notifyObservers() {
        for (UserNotification observer : this.observers) {
            observer.update(this.message, this.repository);
        }
    }
}
