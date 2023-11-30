package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
