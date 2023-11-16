package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEntity extends BaseEntity {

    private String name;

    private String photoUrl;

    @Column(unique = true)
    private String idIMDB;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    private List<TitleEntity> actedInTitles;

    @ManyToMany(mappedBy = "directors", fetch = FetchType.LAZY)
    private List<TitleEntity> directedTitles;

    @ManyToMany(mappedBy = "writers", fetch = FetchType.LAZY)
    private List<TitleEntity> writtenTitles;

    @OneToMany(mappedBy = "person")
    private List<CharacterRoleEntity> characterRoles;
}
