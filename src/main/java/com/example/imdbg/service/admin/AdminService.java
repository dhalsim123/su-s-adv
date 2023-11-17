package com.example.imdbg.service.admin;

import com.example.imdbg.model.entity.users.RoleEntity;
import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.model.entity.users.enums.RoleEnum;
import com.example.imdbg.service.users.RoleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AdminService {

    private final UserService userService;
    private final RoleService roleService;
    private final SessionRegistry sessionRegistry;


    public AdminService(UserService userService, RoleService roleService, SessionRegistry sessionRegistry) {
        this.userService = userService;
        this.roleService = roleService;
        this.sessionRegistry = sessionRegistry;
    }

    public void removeAdminRole(Long id, Principal principal) {

        UserEntity admin = userService.findUserByUsername(principal.getName());

        if (admin.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
            UserEntity userById = userService.findUserById(id);
            userById.getRoles().removeIf(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN));

            userService.saveUser(userById);

            this.expireUserSessions(userById);
        }
    }

    public void addAdminRole(Long id, Principal principal) {

        UserEntity admin = userService.findUserByUsername(principal.getName());

        if (admin.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
            UserEntity userById = userService.findUserById(id);
            RoleEntity adminRole = roleService.findRoleByName(RoleEnum.ADMIN);
            if (userById.getRoles().stream().noneMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
                userById.getRoles().add(adminRole);
            }

            userService.saveUser(userById);

            this.expireUserSessions(userById);
        }
    }

    public void expireUserSessions(UserEntity user) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof User) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(user.getUsername())) {
                    sessionRegistry.getAllSessions(userDetails, false)
                            .forEach(SessionInformation::expireNow);
                }
            }
        }
    }
}
