package com.example.imdbg.model.entity.movies.dtos.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TitleCarouselViewDTO {
    private Long id;
    private String title;
    private Integer year;
    private String mainPosterURLPhotoUrl;
    private String mainTrailerURLVideoImdbId;
    private Long mainTrailerURLId;
    private float imdbRating;
}
