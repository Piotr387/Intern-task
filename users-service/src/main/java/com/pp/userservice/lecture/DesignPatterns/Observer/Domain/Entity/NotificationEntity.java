package com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private int userId;
    private String message;

    public NotificationEntity(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
