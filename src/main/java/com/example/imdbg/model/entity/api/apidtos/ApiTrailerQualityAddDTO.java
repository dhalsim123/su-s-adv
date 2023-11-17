package com.example.imdbg.model.entity.api.apidtos;

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
