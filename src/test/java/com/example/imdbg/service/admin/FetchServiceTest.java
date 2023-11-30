package com.example.imdbg.service.admin;

import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class FetchServiceTest {

    @Mock
    private TitleService titleService;

    @Mock
    private ImdbScrapeService imdbScrapeService;


    private FetchService toTest;

    @BeforeEach
    void setUp(){
        toTest = new FetchService(titleService, imdbScrapeService);
    }

    @Test
    void fetchTop250ImdbTitles() {

        LinkedHashMap<String, String> testMap = new LinkedHashMap<>();
        Mockito.when(imdbScrapeService.getTop250IdsAndRatings()).thenReturn(testMap);

        toTest.fetchTop250ImdbTitles();

        Mockito.verify(imdbScrapeService, Mockito.times(1)).getTop250IdsAndRatings();
        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsAndRatingsMap(testMap);

    }

    @Test
    void fetchTop250ImdbTitles_Catch_Throw() {
        Mockito.when(imdbScrapeService.getTop250IdsAndRatings()).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetchTop250ImdbTitles());
    }

    @Test
    void fetch100MostPopularImdbTitles() {
        LinkedHashMap<String, String> testMap = new LinkedHashMap<>();
        Mockito.when(imdbScrapeService.get100MostPopularIdsAndRatings()).thenReturn(testMap);

        toTest.fetch100MostPopularImdbTitles();

        Mockito.verify(imdbScrapeService, Mockito.times(1)).get100MostPopularIdsAndRatings();
        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsAndRatingsMap(testMap);
    }

    @Test
    void fetch100MostPopularImdbTitles_Catch_Throw() {
        Mockito.when(imdbScrapeService.get100MostPopularIdsAndRatings()).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetch100MostPopularImdbTitles());
    }

    @Test
    void fetchUpcomingImdbTitles() {
        List<String> testList = new ArrayList<>();
        Mockito.when(imdbScrapeService.getUpcomingReleases()).thenReturn(testList);

        toTest.fetchUpcomingImdbTitles();

        Mockito.verify(imdbScrapeService, Mockito.times(1)).getUpcomingReleases();
        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsList(testList);

    }

    @Test
    void fetchUpcomingImdbTitles_Catch_Throw() {
        Mockito.when(imdbScrapeService.getUpcomingReleases()).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetchUpcomingImdbTitles());

    }

    @Test
    void fetchSingleImdbTitle() {

        String testImdbId = "testImdbId";

        toTest.fetchSingleImdbTitle(testImdbId);

        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsList(List.of(testImdbId));
    }

    @Test
    void fetchSingleImdbTitle_Throw_Catch() {

        String testImdbId = "testImdbId";

        Mockito.when(titleService.createNewTitlesFromIdsList(List.of(testImdbId))).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetchSingleImdbTitle(testImdbId));
    }

    @Test
    void fetch50Titles() {
        int testPageNumber = 1;

        LinkedHashMap<String, String> testMap = new LinkedHashMap<>();
        Mockito.when(imdbScrapeService.get50TitleIdsAndRatings(testPageNumber)).thenReturn(testMap);

        toTest.fetch50Titles(testPageNumber);

        Mockito.verify(imdbScrapeService, Mockito.times(1)).get50TitleIdsAndRatings(testPageNumber);
        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsAndRatingsMap(testMap);

    }

    @Test
    void fetch50Titles_Throw_Catch() {
        int testPageNumber = 1;

        Mockito.when(imdbScrapeService.get50TitleIdsAndRatings(testPageNumber)).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetch50Titles(testPageNumber));
    }

    @Test
    void fetch250Titles() {

        int testPageNumber = 1;

        LinkedHashMap<String, String> testMap = new LinkedHashMap<>();
        Mockito.when(imdbScrapeService.get250TitleIdsAndRatings(testPageNumber)).thenReturn(testMap);

        toTest.fetch250Titles(testPageNumber);

        Mockito.verify(imdbScrapeService, Mockito.times(1)).get250TitleIdsAndRatings(testPageNumber);
        Mockito.verify(titleService, Mockito.times(1)).createNewTitlesFromIdsAndRatingsMap(testMap);

    }

    @Test
    void fetch250Titles_Throw_Catch() {
        int testPageNumber = 1;

        Mockito.when(imdbScrapeService.get250TitleIdsAndRatings(testPageNumber)).thenThrow(RuntimeException.class);
        assertDoesNotThrow(() -> toTest.fetch250Titles(testPageNumber));
    }
    @Test
    void updateImdbPopularityRanks100MostPopular() {

        toTest.updateImdbPopularityRanks100MostPopular();

        Mockito.verify(titleService, Mockito.times(1)).updateImdb100MostPopular();

    }

    @Test
    void updateImdbPopularityRanks100MostPopular_Throw_Catch() {

        Mockito.doThrow(RuntimeException.class).when(titleService).updateImdb100MostPopular();
        assertDoesNotThrow(() -> toTest.updateImdbPopularityRanks100MostPopular());

    }

    @Test
    void updateImdbTop250() {

        toTest.updateImdbTop250();

        Mockito.verify(titleService, Mockito.times(1)).updateImdbTop250Ranks();
    }

    @Test
    void updateImdbTop250_Throw_Catch() {

        Mockito.doThrow(RuntimeException.class).when(titleService).updateImdbTop250Ranks();
        assertDoesNotThrow(() -> toTest.updateImdbTop250());
    }

    @Test
    void updateSingleTitle() {

        Long testId = 1L;

        toTest.updateSingleTitle(testId);

        Mockito.verify(titleService, Mockito.times(1)).updateSingleTitle(testId);
    }

    @Test
    void updateSingleTitle_Throw_Catch() {

        Long testId = 1L;

        Mockito.doThrow(RuntimeException.class).when(titleService).updateSingleTitle(testId);
        assertDoesNotThrow(() -> toTest.updateSingleTitle(testId));
    }



}