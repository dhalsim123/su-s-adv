package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.TypeEntity;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.movies.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public void initTypes(){
        if (typeRepository.count() == 0) {
            Arrays.stream(TypeEnum.values())
                    .forEach(typeEnum -> typeRepository.saveAndFlush(TypeEntity.builder().name(typeEnum).build()));
        }
    }

    public TypeEntity findTypeEntityByName(String type){
        return typeRepository
                .findTypeEntityByName(TypeEnum.valueOf(type.toUpperCase()))
                .orElseThrow(() -> new ObjectNotFoundException("Type with name " + type + " was not found"));
    }
}
