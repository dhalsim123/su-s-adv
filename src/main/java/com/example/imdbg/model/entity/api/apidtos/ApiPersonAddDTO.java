package com.example.imdbg.model.entity.api.apidtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiPersonAddDTO {
    private String character;
    private String name;
    @Getter(AccessLevel.NONE)
    private String id;
    @Getter(AccessLevel.NONE)
    private String idIMDB;
    private String urlPhoto;

    public String getId() {
        return id == null ? idIMDB : id;
    }
}