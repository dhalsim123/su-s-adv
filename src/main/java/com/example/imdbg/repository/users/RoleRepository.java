package com.example.imdbg.repository.users;

import com.example.imdbg.model.entity.users.RoleEntity;
import com.example.imdbg.model.entity.users.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findRoleEntityByRole(RoleEnum roleEnum);
}
