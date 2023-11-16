package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.*;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<TitleEntity, Long> {

    Optional<TitleEntity> findTitleEntityByImdbId(String ImdbId);

    List<TitleEntity> findAllByImdbIdIsIn(List<String> ids);
    List<TitleEntity> findAllByImdbIdIsInAndImdbRating(List<String> ids, float rating);
    List<TitleEntity> findAllByReleaseDateAfterAndLastUpdatedBefore(LocalDate releaseDateAfter, LocalDate lastUpdatedBefore);

    List<TitleEntity> findAllByType_NameAndMainTrailerURLNotNullOrderByImdbRatingDesc(TypeEnum typeEnum);

    List<TitleEntity> findAllByType_NameAndMainTrailerURLNotNullAndPopularityNotNullOrderByPopularityAsc(TypeEnum typeEnum);
    List<TitleEntity> findAllByPopularityNotNull();

    Optional<TitleEntity> findTitleEntityByMainTrailerURL_VideoImdbId (String videoImdbId);

    @Query("SELECT t FROM TitleEntity t ORDER BY t.imdbRating DESC LIMIT 250")
    List<TitleEntity> findTop250ImdbRatedTitles();

    @Query("SELECT t FROM TitleEntity t WHERE t.type.name = 'MOVIE' ORDER BY t.imdbRating DESC LIMIT 250")
    List<TitleEntity> findTop250ImdbRatedMovies();

    @Query("SELECT a.idIMDB FROM TitleEntity t JOIN t.actors a WHERE t.imdbId = :titleImdbId")
    List<String> findIdsOfActorsForTitle (@Param("titleImdbId") String titleImdbId);

    List<TitleEntity> findFirst7ByTitleContaining(String search);
}
