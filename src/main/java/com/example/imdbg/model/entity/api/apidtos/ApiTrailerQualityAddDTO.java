package com.example.imdbg.model.entity.api.apidtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiTrailerQualityAddDTO {
    private String quality;
    private String videoURL;
}
