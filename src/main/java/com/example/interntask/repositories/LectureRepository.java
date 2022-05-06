package com.example.interntask.repositories;

import com.example.interntask.entity.LectureEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends CrudRepository<LectureEntity, Long> {
    List<LectureEntity> findAll();
}
