package com.example.imdbg.service.admin;

import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FetchService {

    private final TitleService titleService;
    private final ImdbScrapeService imdbScrapeService;
    private final Logger LOGGER = LoggerFactory.getLogger(FetchService.class);

    @Getter
    private List<String> fetchThreadLog = new ArrayList<>();


    public FetchService(TitleService titleService, ImdbScrapeService imdbScrapeService) {
        this.titleService = titleService;
        this.imdbScrapeService = imdbScrapeService;
    }

    @CacheEvict("top250TitleViewDTOs")
    public void fetchTop250ImdbTitles(){
        this.newFetchThreadLog("Top250 Fetch: ");

        try {
            LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();
            String info = titleService.createNewTitlesFromIdsAndRatingsMap(top250IdsAndRatings);
            this.fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the top 250 Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch top250ImdbTitles because of this error: " + e);
        }

    }

    @CacheEvict("100MostPopularTitleViewDTOs")
    public void fetch100MostPopularImdbTitles(){
        this.newFetchThreadLog("100 Most Popular Fetch: ");

        try {
            LinkedHashMap<String, String> mostPopularIdsAndRatings = imdbScrapeService.get100MostPopularIdsAndRatings();
            String info = titleService.createNewTitlesFromIdsAndRatingsMap(mostPopularIdsAndRatings);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the 100 most popular Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }

    }

    public void fetchUpcomingImdbTitles(){
        this.newFetchThreadLog("Upcoming titles Fetch: ");

        try {
            List<String> upcomingReleases = imdbScrapeService.getUpcomingReleases();
            String info = titleService.createNewTitlesFromIdsList(upcomingReleases);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the most popular upcoming releases of Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch UpcomingImdbTitles because of this error: " + e);
        }
    }

    public void fetchSingleImdbTitle(String imdbId){
        this.newFetchThreadLog("Single Title Fetch with id " + imdbId + ": ");

        try {
            String info = titleService.createNewTitlesFromIdsList(new ArrayList<>(List.of(imdbId)));
            fetchThreadLog.add(info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch this Imdb title " + imdbId + " because of this error:" + e);
        }
    }

    public void clearFetchThreadLog(){
        this.fetchThreadLog = new ArrayList<>();
    }

    public void fetch50Titles(int pageNumber){
        this.newFetchThreadLog("Page " + pageNumber + " Out of the Search Results: ");

        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get50TitleIdsAndRatings(pageNumber);
            String info = titleService.createNewTitlesFromIdsAndRatingsMap(idsAndRatingsMap);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching page " + pageNumber + "  of the Imdb titles search results. " + (info.isEmpty() ? "" : "But failed to save: " + info));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    public void fetch250Titles(int pageNumber){
        this.newFetchThreadLog("Page Fetch: ");

        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get250TitleIdsAndRatings(pageNumber);
            String info = titleService.createNewTitlesFromIdsAndRatingsMap(idsAndRatingsMap);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching page " + pageNumber + " of the Imdb titles search results. " + (info.isEmpty() ? "" : "But failed to save: " + info));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    @CacheEvict("100MostPopularTitleViewDTOs")
    public void updateImdbPopularityRanks100MostPopular(){
        this.newFetchThreadLog("100 Most Popular Ranks Update: ");

        try {
            titleService.updateImdb100MostPopular();
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the 100 most popular titles because of this error: " + e);
            fetchThreadLog.add("Couldn't update the 100 most popular titles because of this error: " + e.getMessage());
        }
    }

    @CacheEvict("top250TitleViewDTOs")
    public void updateImdbTop250(){
        this.newFetchThreadLog("Top 250 Update: ");

        try {
            titleService.updateImdbTop250Ranks();
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the top 250 titles because of this error: " + e);
            fetchThreadLog.add("Couldn't update the top 250 because of this error: " + e.getMessage());
        }
    }

    public void updateSingleTitle(Long id){
        this.newFetchThreadLog("Title with id: " + id);

        try {
            titleService.updateSingleTitle(id);
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the title because of this error " + e);
            fetchThreadLog.add("Couldn't update the title because of this error: " + e.getMessage());
        }
    }

    private void newFetchThreadLog(String method) {
        this.clearFetchThreadLog();
        this.getFetchThreadLog().add(method);
    }

}
