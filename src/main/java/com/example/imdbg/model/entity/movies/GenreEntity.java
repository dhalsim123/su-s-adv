package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import com.example.imdbg.model.entity.movies.enums.GenreEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column
    private GenreEnum name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    private List<TitleEntity> titles;
}
