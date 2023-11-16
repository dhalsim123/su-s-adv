package com.example.imdbg.model.entity.users;

import com.example.imdbg.model.entity.BaseEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "auth_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column (nullable = false)
    private String password;

    @Column (unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "auth_users_roles")
    private List<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<TitleEntity> watchlist;
}
