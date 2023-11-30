package com.example.imdbg.model.entity.movies.dtos.view.characterRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CharacterRoleViewDTO {

    private Long titleId;

    private String personName;

    private String characterName;

    private Long personId;

}
