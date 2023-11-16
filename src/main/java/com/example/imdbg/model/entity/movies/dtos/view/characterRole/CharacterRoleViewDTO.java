package com.example.imdbg.model.entity.movies.dtos.view.characterRole;

import com.example.imdbg.model.entity.movies.PersonEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import jakarta.persistence.ManyToOne;
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
