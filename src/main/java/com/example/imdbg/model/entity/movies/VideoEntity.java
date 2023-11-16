package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoEntity extends BaseEntity {

    @ManyToOne
    private TitleEntity title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String videoImdbId;

    @Column
    private boolean isTrailer;
}
