package com.example.imdbg.model.entity.api.apidtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiBoxOfficeAddDTO {

    private String openingWeekend;
    private String grossUsa;
    private String worldwide;
}
