package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.GenreEntity;
import com.example.imdbg.model.entity.movies.enums.GenreEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.movies.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;
    private GenreService toTest;

    @BeforeEach
    void setUp(){
        toTest = new GenreService(genreRepository);
    }

    @Test
    void initGenres() {
        Mockito.when(genreRepository.count()).thenReturn(0L);

        toTest.initGenres();

        Mockito.verify(genreRepository, Mockito.times(GenreEnum.values().length)).saveAndFlush(any());
    }

    @Test
    void findGenreEntitiesByName() {
        List<String> genres = List.of(GenreEnum.ACTION.name(), GenreEnum.COMEDY.name());

        GenreEntity genreEntity1 = new GenreEntity();
        GenreEntity genreEntity2 = new GenreEntity();

        Mockito.when(genreRepository.findGenreEntityByName(GenreEnum.ACTION)).thenReturn(Optional.of(genreEntity1));
        Mockito.when(genreRepository.findGenreEntityByName(GenreEnum.COMEDY)).thenReturn(Optional.of(genreEntity2));

        List<GenreEntity> genreEntitiesByName = toTest.findGenreEntitiesByName(genres);

        Mockito.verify(genreRepository, Mockito.times(genres.size())).findGenreEntityByName(any(GenreEnum.class));

        assertEquals(genreEntitiesByName.get(0), genreEntity1);
        assertEquals(genreEntitiesByName.get(1), genreEntity2);
    }

    @Test
    void findGenreEntityByName() {

        GenreEntity expectedEntity = new GenreEntity();

        Mockito.when(genreRepository.findGenreEntityByName(GenreEnum.ACTION)).thenReturn(Optional.of(expectedEntity));

        GenreEntity entityFound = toTest.findGenreEntityByName(GenreEnum.ACTION.name());

        assertEquals(expectedEntity, entityFound);
    }

    @Test
    void findGenreEntityByName_Throw() {
        Mockito.when(genreRepository.findGenreEntityByName(GenreEnum.ACTION)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> toTest.findGenreEntityByName(GenreEnum.ACTION.name()));

    }
}