package com.example.imdbg.service.admin;

import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void fetchTop250ImdbTitles(){
        newFetchThreadLog("Top250 Fetch: ");

        try {
            LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();
            String info = titleService.createNewTitlesWithIdsAndRatingsMap(top250IdsAndRatings);
            this.fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the top 250 Imdb titles. " + info);
            updateImdbTop250();
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch top250ImdbTitles because of this error:" + e);
        }

    }
    public void fetch100MostPopularImdbTitles(){
        newFetchThreadLog("100 Most Popular Fetch: ");

        try {
            LinkedHashMap<String, String> mostPopularIdsAndRatings = imdbScrapeService.get100MostPopularIdsAndRatings();
            String info = titleService.createNewTitlesWithIdsAndRatingsMap(mostPopularIdsAndRatings);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the 100 most popular Imdb titles. " + info);
            updateImdbPopularityRanks100MostPopular();
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }

    }

    public void fetchUpcomingImdbTitles(){
        newFetchThreadLog("Upcoming titles Fetch: ");

        try {
            List<String> upcomingReleases = imdbScrapeService.getUpcomingReleases();
            String info = titleService.createNewTitlesWithIdsList(upcomingReleases);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching the most popular upcoming releases of Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch UpcomingImdbTitles because of this error: " + e);
        }
    }

    public void fetchSingleImdbTitle(String imdbId){
        newFetchThreadLog("Single Title Fetch with id " + imdbId + ": ");

        try {
            String info = titleService.createNewTitlesWithIdsList(new ArrayList<>(List.of(imdbId)));
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
        newFetchThreadLog("Page " + pageNumber + " Out of the Search Results: ");

        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get50TitleIdsAndRatings(pageNumber);
            String info = titleService.createNewTitlesWithIdsAndRatingsMap(idsAndRatingsMap);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching page " + pageNumber + "  of the Imdb titles search results. " + (info.isEmpty() ? "" : "But failed to save: " + info));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    public void fetch250Titles(int pageNumber){
        newFetchThreadLog("Top250 Fetch: ");

        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get250TitleIdsAndRatings(pageNumber);
            String info = titleService.createNewTitlesWithIdsAndRatingsMap(idsAndRatingsMap);
            fetchThreadLog.add(info);
            LOGGER.info("Finished fetching page " + pageNumber + " of the Imdb titles search results. " + (info.isEmpty() ? "" : "But failed to save: " + info));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    public void updateImdbPopularityRanks100MostPopular(){
        newFetchThreadLog("100 Most Popular Ranks Update: ");

        try {
            titleService.updateImdb100MostPopular();
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the 100 most popular titles because of this error: " + e);
            fetchThreadLog.add("Couldn't update the 100 most popular titles because of this error: " + e);
        }
    }

    public void updateImdbTop250(){
        newFetchThreadLog("Top 250 Update: ");

        try {
            titleService.updateImdbTop250Ranks();
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the top 250 titles because of this error: " + e);
            fetchThreadLog.add("Couldn't update the top 250 because of this error: " + e);
        }
    }

    public void updateSingleTitle(String imdbId){
        newFetchThreadLog("Title with id: " + imdbId);

        try {
            titleService.updateSingleTitle(imdbId);
            fetchThreadLog.add("Updated");
        }
        catch (Exception e){
            LOGGER.error("Couldn't update the title because of this error" + e);
            fetchThreadLog.add("Couldn't update the title because of this error" + e);
        }
    }

    private void newFetchThreadLog(String method) {
        this.clearFetchThreadLog();
        this.getFetchThreadLog().add(method);
    }

}
