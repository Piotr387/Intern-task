package com.pp.userservice.user;

import com.pp.userservice.user.entity.UserEntity;

// start L4 Interfejs funkcyjny 2.
@FunctionalInterface
public interface HasUserRole {
  boolean hasUserRole(UserEntity roleEntity);
}

