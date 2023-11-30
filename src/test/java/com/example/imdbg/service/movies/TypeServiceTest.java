package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.TypeEntity;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.movies.TypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TypeServiceTest {

    @Mock
    private TypeRepository typeRepository;

    private TypeService toTest;

    @BeforeEach
    void setUp(){
        toTest = new TypeService(typeRepository);
    }

    @Test
    void initTypes() {
        Mockito.when(typeRepository.count()).thenReturn(0L);

        toTest.initTypes();

        Mockito.verify(typeRepository, Mockito.times(TypeEnum.values().length)).saveAndFlush(any());
    }

    @Test
    void findTypeEntityByName() {
        TypeEntity expectedType = new TypeEntity();

        Mockito.when(typeRepository.findTypeEntityByName(TypeEnum.MOVIE)).thenReturn(Optional.of(expectedType));

        TypeEntity result = toTest.findTypeEntityByName(TypeEnum.MOVIE.name());

        assertEquals(result, expectedType);
    }

    @Test
    void findTypeEntityByName_throw() {

        Mockito.when(typeRepository.findTypeEntityByName(TypeEnum.MOVIE)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> toTest.findTypeEntityByName(TypeEnum.MOVIE.name()));
    }
}