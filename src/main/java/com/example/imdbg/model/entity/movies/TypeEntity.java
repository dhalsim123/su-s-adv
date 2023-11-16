package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

    @Entity
    @Table(name = "types")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class TypeEntity extends BaseEntity {

        @Enumerated(EnumType.STRING)
        @Column
        private TypeEnum name;

        @OneToMany(mappedBy = "type", cascade = CascadeType.MERGE)
        private List<TitleEntity> titles;
    }
