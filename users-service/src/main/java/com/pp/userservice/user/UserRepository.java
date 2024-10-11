package com.pp.userservice.user;

import com.pp.userservice.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * interface user repository that extends CrudRepository that contains
 * complete set of methods to manipulate lecture entity. For bigger database we could implemnt
 * PagingAndSortingRepository which would handle problem with more records
 */
// start L1 Proxy - by making repository package-private we're forcing to use UserServiceImplementation
// to get any resources related with UserEntity
@Repository
interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findAll();

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);
}
