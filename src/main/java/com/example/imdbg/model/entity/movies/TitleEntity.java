package com.example.imdbg.model.entity.movies;

import com.example.imdbg.model.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "titles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitleEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String imdbId;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    private TypeEntity type;

    @ManyToMany
    private List<GenreEntity> genres;

    @ManyToMany
    private List<PersonEntity> actors;

    @ManyToMany
    private List<PersonEntity> directors;

    @ManyToMany
    private List<PersonEntity> writers;

    @OneToMany(mappedBy = "title")
    private List<CharacterRoleEntity> characterRoles;

    @Column
    private Integer year;

    @Column
    private LocalDate releaseDate;

    @Column
    private Long runtime;

    @Column(columnDefinition = "TEXT")
    private String plot;

    @Column(columnDefinition = "TEXT")
    private String simplePlot;

    @Column
    @Min(0)
    @Max(10)
    private float imdbRating;

    @Column
    @Min(0)
    @Max(100)
    private Integer metascore;

    @Min(0)
    private Integer imdbVotes;

    @Column
    private Integer imdbTop250Rank;

    @Column
    @Min(0)
    private Integer popularity;

    @Column
    private String boxOfficeOpeningWeekend;
    @Column
    private String boxOfficeGrossUsa;
    @Column
    private String boxOfficeWorldwide;

    @OneToOne(cascade = CascadeType.ALL)
    private PhotoEntity mainPosterURL;

    @OneToOne(cascade = CascadeType.ALL)
    private VideoEntity mainTrailerURL;

    @OneToMany(mappedBy = "title")
    private List<PhotoEntity> photos;

    @OneToMany(mappedBy = "title")
    private List<VideoEntity> videos;

    @Column
    private Integer pageViews;

    @Column
    private LocalDate lastUpdated;

    @PrePersist
    public void updateMainPosterAndTrailerTitle() {
        if (this.mainPosterURL != null) {
            this.mainPosterURL.setTitle(this);
        }
        if (this.mainTrailerURL != null) {
            this.mainTrailerURL.setTitle(this);
        }
    }

}
