package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "character_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterRoleEntity extends BaseEntity {

    @ManyToOne
    private TitleEntity title;

    @ManyToOne
    private PersonEntity person;

    private String characterName;
}
