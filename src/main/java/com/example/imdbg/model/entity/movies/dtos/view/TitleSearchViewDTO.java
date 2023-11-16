package com.example.imdbg.model.entity.movies.dtos.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TitleSearchViewDTO {

    private Long id;
    private String title;
    private Integer year;
    private String mainPosterURLPhotoUrl;

}
