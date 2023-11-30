package com.example.imdbg.model.entity.movies.dtos.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class TitleVideoViewDTO {
    private Long id;
    private String title;
    private Integer year;
    private String mainPosterURLPhotoUrl;
    private String mainTrailerURLVideoImdbId;
    private String mainTrailerURLVideoDescription;
    private List<GenreViewDTO> genres;

    public String genresToString(){
        return genres.stream()
                .map(genreViewDTO -> genreViewDTO.getName().name())
                .collect(Collectors.joining(", "));
    }
}
