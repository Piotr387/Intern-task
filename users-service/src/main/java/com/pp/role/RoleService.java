package com.pp.role;

import com.pp.responde.ErrorMessages;
import com.pp.responde.UserServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(() -> {
            throw new UserServiceException(ErrorMessages.NO_ROLE_FOUND_WITH_PROVIDED_NAME.getErrorMessage());
        });
    }

    @Transactional
    public RoleEntity createRoleIfNotFound(String name) {
        RoleEntity roleEntity = roleRepository.findByName(name).orElseGet(() -> new RoleEntity(name));
        roleRepository.save(roleEntity);
        return roleEntity;
    }
}
