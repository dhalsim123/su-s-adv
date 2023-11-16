package com.example.imdbg.model.entity.movies.dtos.view.person;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonViewDTO {

    private Long id;
    private String name;

    private String photoUrl;
}
