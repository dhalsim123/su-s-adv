package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.GenreEntity;
import com.example.imdbg.model.entity.movies.enums.GenreEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
    Optional<GenreEntity> findGenreEntityByName(GenreEnum genreEnum);
}
