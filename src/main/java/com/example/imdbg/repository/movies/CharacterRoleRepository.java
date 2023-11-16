package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.CharacterRoleEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRoleRepository extends JpaRepository<CharacterRoleEntity, Long> {

    List<CharacterRoleEntity> findAllByTitle(TitleEntity title);
}
