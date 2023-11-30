package com.example.imdbg.repository.movies;

import com.example.imdbg.model.entity.movies.TitleEntity;
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

    @Query("SELECT t.imdbId FROM TitleEntity t WHERE t.imdbId IN :ids")
    List<String> findAllImdbIdsByImdbIdIsIn(List<String> ids);
    List<TitleEntity> findAllByImdbIdIsInAndImdbRating(List<String> ids, float rating);
    List<TitleEntity> findAllByReleaseDateAfterAndLastUpdatedBefore(LocalDate releaseDateAfter, LocalDate lastUpdatedBefore);

    List<TitleEntity> findAllByType_NameAndMainTrailerURLNotNullOrderByImdbRatingDesc(TypeEnum typeEnum);

    List<TitleEntity> findAllByType_NameAndMainTrailerURLNotNullAndPopularityNotNullOrderByPopularityAsc(TypeEnum typeEnum);
    List<TitleEntity> findAllByPopularityNotNull();

    Optional<TitleEntity> findTitleEntityByMainTrailerURL_VideoImdbId (String videoImdbId);

    @Query("SELECT t FROM TitleEntity t WHERE t.imdbTop250Rank != null ORDER BY t.imdbTop250Rank")
    List<TitleEntity> findTop250ImdbList();

    @Query("SELECT t FROM TitleEntity t WHERE t.imdbTop250Rank != null ORDER BY t.imdbTop250Rank LIMIT 18")
    List<TitleEntity> findTop250ImdbListLimit18();

    @Query("SELECT t FROM TitleEntity t WHERE t.popularity != null ORDER BY t.popularity")
    List<TitleEntity> find100MostPopularImdbList();

    @Query("SELECT t FROM TitleEntity t WHERE t.popularity != null ORDER BY t.popularity LIMIT 24")
    List<TitleEntity> findTop24OnImdbThisWeek();

    @Query("SELECT t FROM TitleEntity t WHERE t.pageViews != null ORDER BY t.pageViews DESC LIMIT 100")
    List<TitleEntity> find100MostPopularOnThisSite();

    @Query("SELECT t FROM TitleEntity t WHERE t.pageViews != null ORDER BY t.pageViews DESC LIMIT 6")
    List<TitleEntity> find6MostPopularOnThisSite();

    @Query("SELECT a.idIMDB FROM TitleEntity t JOIN t.actors a WHERE t.imdbId = :titleImdbId")
    List<String> findIdsOfActorsForTitle (@Param("titleImdbId") String titleImdbId);

    List<TitleEntity> findFirst7ByTitleContaining(String search);
}

