package com.example.imdbg.model.entity.users.dto.view;

import com.example.imdbg.model.entity.users.enums.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleViewDTO {

    private RoleEnum role;
}
