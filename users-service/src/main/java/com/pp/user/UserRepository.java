package com.pp.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * interface user repository that extends CrudRepository that contains
 * complete set of methods to manipulate lecture entity. For bigger database we could implemnt
 *  PagingAndSortingRepository which would handle problem with more records
 */

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    Optional<UserEntity> findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
}
