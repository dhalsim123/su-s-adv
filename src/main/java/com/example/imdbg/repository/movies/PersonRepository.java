package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Optional<PersonEntity> findPersonEntityByIdIMDB(String idIMDB);
}
