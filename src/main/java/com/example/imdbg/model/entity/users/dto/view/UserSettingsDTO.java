package com.example.imdbg.model.entity.users.dto.view;

import com.example.imdbg.model.entity.users.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserSettingsDTO {

    private Long id;
    private String username;
    private String email;
    private List<RoleViewDTO> roles;

    public boolean isAdmin(){
        return roles.stream().anyMatch(roleViewDTO -> roleViewDTO.getRole().equals(RoleEnum.ADMIN));
    }
}
