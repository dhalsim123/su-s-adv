package com.example.imdbg.model.entity.api.apidtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiTrailerAddDTO {
    private String description;
    private String type;
    private String duration;
    private List<ApiTrailerQualityAddDTO> qualities;
}
