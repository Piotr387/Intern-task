package com.example.interntask.lecture;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * interface lecture repository that extends CrudRepository that contains
 * complete set of methods to manipulate lecture entity.
 */

@Repository
public interface LectureRepository extends CrudRepository<LectureEntity, Long> {
    List<LectureEntity> findAll();
    Optional<LectureEntity> findByName(String lectureName);
}
