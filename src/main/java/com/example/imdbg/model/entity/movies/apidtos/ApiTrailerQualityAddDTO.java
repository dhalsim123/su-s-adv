package com.example.imdbg.model.entity.movies.apidtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiTrailerQualityAddDTO {
    private String quality;
    private String videoURL;
}
