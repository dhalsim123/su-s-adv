package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.GenreEntity;
import com.example.imdbg.model.entity.movies.enums.GenreEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.movies.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void initGenres(){
        if (genreRepository.count() == 0){
            Arrays.stream(GenreEnum.values())
                    .forEach(genreEnum -> genreRepository.saveAndFlush(GenreEntity.builder().name(genreEnum).build()));
        }
    }

    public List<GenreEntity> findGenreEntitiesByName (List<String> genres){
        return genres
                .stream()
                .map(this::findGenreEntityByName)
                .collect(Collectors.toList());
    }

    public GenreEntity findGenreEntityByName(String genre){
        return genreRepository
                .findGenreEntityByName(GenreEnum.valueOf(genre.toUpperCase()))
                .orElseThrow(() -> new ObjectNotFoundException("Genre with name " + genre + " was not found"));
    }
}
