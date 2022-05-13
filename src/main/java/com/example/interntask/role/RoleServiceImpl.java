package com.example.interntask.role;

import com.example.interntask.responde.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public RoleEntity findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> {
            throw new RuntimeException(ErrorMessages.NO_ROLE_FOUND_WITH_PROVIDED_NAME.getErrorMessage());
        });
    }

    @Override
    @Transactional
    public RoleEntity createRoleIfNotFound(String name) {
        RoleEntity roleEntity = roleRepository.findByName(name).orElseGet( () -> new RoleEntity(name));
        roleRepository.save(roleEntity);
        return roleEntity;
    }
}
