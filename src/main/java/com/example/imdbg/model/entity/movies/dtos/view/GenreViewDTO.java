package com.example.imdbg.model.entity.movies.dtos.view;

import com.example.imdbg.model.entity.movies.enums.GenreEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreViewDTO {

    private GenreEnum name;
}
