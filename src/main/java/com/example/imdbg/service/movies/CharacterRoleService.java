package com.example.imdbg.service.movies;


import com.example.imdbg.model.entity.movies.CharacterRoleEntity;
import com.example.imdbg.model.entity.movies.PersonEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.repository.movies.CharacterRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterRoleService {

    private final CharacterRoleRepository characterRoleRepository;

    public CharacterRoleService(CharacterRoleRepository characterRoleRepository) {
        this.characterRoleRepository = characterRoleRepository;

    }

    public void createCharacterRole(TitleEntity title, PersonEntity person, String character) {
        CharacterRoleEntity characterRole = CharacterRoleEntity.builder()
                .person(person)
                .title(title)
                .characterName(character)
                .build();

        characterRoleRepository.saveAndFlush(characterRole);
    }

    public void deleteAllCharacterRolesForTitle(TitleEntity title){
        List<CharacterRoleEntity> allByTitle = characterRoleRepository.findAllByTitle(title);
        characterRoleRepository.deleteAll(allByTitle);
    }
}
