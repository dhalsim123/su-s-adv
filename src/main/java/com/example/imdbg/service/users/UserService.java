package com.example.imdbg.service.users;

import com.example.imdbg.model.entity.BaseEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.model.entity.users.RoleEntity;
import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.model.entity.users.dto.binding.ChangeUsernameDTO;
import com.example.imdbg.model.entity.users.dto.binding.UserRegisterDTO;
import com.example.imdbg.model.entity.users.dto.view.UserSettingsDTO;
import com.example.imdbg.model.entity.users.enums.RoleEnum;
import com.example.imdbg.repository.users.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final SessionRegistry sessionRegistry;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public UserService(UserRepository userRepository, RoleService roleService, SessionRegistry sessionRegistry, PasswordEncoder passwordEncoder, ModelMapper modelMapper, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.sessionRegistry = sessionRegistry;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    public void initAdmin() {

        if (userRepository.count() == 0) {
            UserEntity admin = UserEntity.builder()
                    .username("admin123")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin123@admin.com")
                    .roles(roleService.findAllRoles())
                    .build();

            userRepository.saveAndFlush(admin);
        }

    }

    public UserEntity findUserByUsername(String username) {
        return userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username" + username + "was not found!"));
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id" + id + "was not found!"));
    }

    public UserEntity findUserByUsernameForUpdate(String username) {
        return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getSingleResult();
    }


    public List<Long> getPrincipalWatchlistIds(Principal principal) {
        return this.findUserByUsername(principal.getName()).getWatchlist().stream().map(BaseEntity::getId).toList();
    }

    public List<TitleEntity> getPrincipalWatchlist(Principal principal) {
        return this.findUserByUsername(principal.getName()).getWatchlist();
    }


    public void registerUser(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        UserEntity user = modelMapper.map(userRegisterDTO, UserEntity.class);
        user.setRoles(List.of(roleService.findRoleByName(RoleEnum.USER)));
        userRepository.save(user);
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public UserSettingsDTO getUserSettingsDTO(Principal principal) {
        UserEntity userByUsername = this.findUserByUsername(principal.getName());
        return modelMapper.map(userByUsername, UserSettingsDTO.class);
    }

    public void changeUsername(ChangeUsernameDTO changeUsernameDTO, Principal principal) {
        UserEntity userByUsername = this.findUserByUsername(principal.getName());
        userByUsername.setUsername(changeUsernameDTO.getUsername());

        userRepository.saveAndFlush(userByUsername);

    }

    public List<UserSettingsDTO> getAllUserSettingsDTO() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream().map(user -> modelMapper.map(user, UserSettingsDTO.class)).toList();
    }

    public UserSettingsDTO getUserSettingsDTOById(Long id) {
        UserEntity userByUsername = this.findUserById(id);
        return modelMapper.map(userByUsername, UserSettingsDTO.class);
    }

    public void removeAdminRole(Long id, Principal principal) {

        UserEntity admin = this.findUserByUsername(principal.getName());

        if (admin.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
            UserEntity userById = this.findUserById(id);
            userById.getRoles().removeIf(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN));

            userRepository.saveAndFlush(userById);

            this.expireUserSessions(userById);
        }
    }

    public void addAdminRole(Long id, Principal principal) {

        UserEntity admin = this.findUserByUsername(principal.getName());

        if (admin.getRoles().stream().anyMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
            UserEntity userById = this.findUserById(id);
            RoleEntity adminRole = roleService.findRoleByName(RoleEnum.ADMIN);
            if (userById.getRoles().stream().noneMatch(roleEntity -> roleEntity.getRole().equals(RoleEnum.ADMIN))) {
                userById.getRoles().add(adminRole);
            }

            userRepository.saveAndFlush(userById);

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

