package com.example.interntask.repositories;

import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends CrudRepository<LectureEntity, Long> {
    List<LectureEntity> findAll();
    Optional<LectureEntity> findByName(String lectureName);
}
