package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoEntity extends BaseEntity {

    @ManyToOne
    private TitleEntity title;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String photoUrl;

    @Column
    private boolean isPoster;
}
