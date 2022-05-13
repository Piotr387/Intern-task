package com.example.interntask.role;

public interface RoleService {
    RoleEntity findByName(String roleName);
    RoleEntity createRoleIfNotFound(String name);
}
