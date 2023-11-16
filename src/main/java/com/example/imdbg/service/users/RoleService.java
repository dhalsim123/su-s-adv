package com.example.imdbg.service.users;

import com.example.imdbg.model.entity.users.RoleEntity;
import com.example.imdbg.model.entity.users.enums.RoleEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.users.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void initRoles(){
        if (roleRepository.count() == 0){
            List<RoleEntity> rolesList = List.of(
                    RoleEntity.builder()
                            .role(RoleEnum.ADMIN)
                            .build(),

                    RoleEntity.builder()
                            .role(RoleEnum.USER)
                            .build()
            );

            roleRepository.saveAllAndFlush(rolesList);
        }
    }

    public List<RoleEntity> findAllRoles(){
        return roleRepository.findAll();
    }

    public RoleEntity findRoleByName(RoleEnum roleEnum){
        return roleRepository.findRoleEntityByRole(roleEnum).orElseThrow(() -> new ObjectNotFoundException("Role with name " + roleEnum.name() + " was not found!"));
    }
}
