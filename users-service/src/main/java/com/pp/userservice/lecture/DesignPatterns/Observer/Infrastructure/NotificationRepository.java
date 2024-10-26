package com.pp.userservice.lecture.DesignPatterns.Observer.Infrastructure;

import com.pp.userservice.lecture.DesignPatterns.Observer.Domain.Entity.NotificationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAll();

    NotificationEntity save(NotificationEntity entity);
}
