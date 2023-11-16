package com.example.imdbg.model.entity.movies.dtos.view.type;

import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TypeViewDTO {

    private TypeEnum name;
}
