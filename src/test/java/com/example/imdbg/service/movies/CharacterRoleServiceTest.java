package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.CharacterRoleEntity;
import com.example.imdbg.model.entity.movies.PersonEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.repository.movies.CharacterRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CharacterRoleServiceTest {

    @Mock
    private CharacterRoleRepository characterRoleRepository;

    private CharacterRoleService toTest;

    @BeforeEach
    void setUp(){
        toTest = new CharacterRoleService(characterRoleRepository);
    }


    @Test
    void createCharacterRole() {
        TitleEntity title = new TitleEntity();
        PersonEntity person = new PersonEntity();
        String character = "Test";

        toTest.createCharacterRole(title, person, character);

        ArgumentCaptor<CharacterRoleEntity> captor = ArgumentCaptor.forClass(CharacterRoleEntity.class);
        Mockito.verify(characterRoleRepository).saveAndFlush(captor.capture());

        CharacterRoleEntity savedEntity = captor.getValue();
        assertEquals(title, savedEntity.getTitle());
        assertEquals(person, savedEntity.getPerson());
        assertEquals(character, savedEntity.getCharacterName());
    }

    @Test
    void deleteAllCharacterRolesForTitle() {
        TitleEntity title = new TitleEntity();
        List<CharacterRoleEntity> roles = new ArrayList<>();
        roles.add(new CharacterRoleEntity());

        Mockito.when(characterRoleRepository.findAllByTitle(title)).thenReturn(roles);

        toTest.deleteAllCharacterRolesForTitle(title);

        Mockito.verify(characterRoleRepository).findAllByTitle(title);
        Mockito.verify(characterRoleRepository).deleteAll(roles);
    }
}