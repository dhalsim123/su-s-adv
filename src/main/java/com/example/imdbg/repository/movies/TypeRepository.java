package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.TypeEntity;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    Optional<TypeEntity> findTypeEntityByName(TypeEnum typeEnum);

}
