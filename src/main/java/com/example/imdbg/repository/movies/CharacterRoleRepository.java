package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.CharacterRoleEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRoleRepository extends JpaRepository<CharacterRoleEntity, Long> {

    List<CharacterRoleEntity> findAllByTitle(TitleEntity title);
}
