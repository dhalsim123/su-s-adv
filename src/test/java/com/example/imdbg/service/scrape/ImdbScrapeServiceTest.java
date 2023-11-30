package com.example.imdbg.service.scrape;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
class ImdbScrapeServiceTest {

    @Autowired
    private ImdbScrapeService toTest;

    @Test
    void getTop250Ids() {
        List<String> top250Ids = toTest.getTop250Ids();

        Assertions.assertFalse(top250Ids.isEmpty());
    }

    @Test
    void get100MostPopularIds() {
        List<String> mostPopularIds = toTest.get100MostPopularIds();

        Assertions.assertFalse(mostPopularIds.isEmpty());
    }

    @Test
    void getUpcomingReleases() {
        List<String> upcomingReleases = toTest.getUpcomingReleases();

        Assertions.assertFalse(upcomingReleases.isEmpty());
    }

    @Test
    void getTop250IdsAndRatings() {
        LinkedHashMap<String, String> top250IdsAndRatings = toTest.getTop250IdsAndRatings();

        Assertions.assertFalse(top250IdsAndRatings.isEmpty());

    }

    @Test
    void get100MostPopularIdsAndRatings() {
        LinkedHashMap<String, String> mostPopularIdsAndRatings = toTest.get100MostPopularIdsAndRatings();

        Assertions.assertFalse(mostPopularIdsAndRatings.isEmpty());

    }

    @Test
    void get100MostPopularIdsAndPopularity() {
        LinkedHashMap<String, String> mostPopularIdsAndPopularity = toTest.get100MostPopularIdsAndPopularity();

        Assertions.assertFalse(mostPopularIdsAndPopularity.isEmpty());

    }

    @Test
    void get50TitleIdsAndRatings() {
        LinkedHashMap<String, String> titleIdsAndRatings = toTest.get50TitleIdsAndRatings(1);

        Assertions.assertFalse(titleIdsAndRatings.isEmpty());

    }

    @Test
    void get250TitleIdsAndRatings() {
        LinkedHashMap<String, String> titleIdsAndRatings = toTest.get250TitleIdsAndRatings(1);

        Assertions.assertFalse(titleIdsAndRatings.isEmpty());

    }
}