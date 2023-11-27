package com.example.imdbg.service.scrape;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImdbScrapeService {

    public List<String> getTop250Ids() {
        String url = "https://www.imdb.com/chart/top/";
        return getImdbIds(url);
    }

    public List<String> get100MostPopularIds() {
        String url = "https://www.imdb.com/chart/moviemeter";

        return getImdbIds(url);
    }

    public List<String> getUpcomingReleases() {
        String url = "https://www.imdb.com/calendar/";
        return getImdbIds(url);
    }

    public LinkedHashMap<String, String> getTop250IdsAndRatings() {
        String url = "https://www.imdb.com/chart/top/";
        return getImdbIdsAndRatings(url);
    }

    public LinkedHashMap<String, String> get100MostPopularIdsAndRatings() {
        String url = "https://www.imdb.com/chart/moviemeter";
        return getImdbIdsAndRatings(url);
    }

    public LinkedHashMap<String, String> get100MostPopularIdsAndPopularity() {
        String url = "https://www.imdb.com/chart/moviemeter";
        return getImdbIdsAndPopularity(url);
    }

    public LinkedHashMap<String, String> get50TitleIdsAndRatings(int pageNumber) {
        int start = (pageNumber - 1) * 50 + 1;
        int end = start + 49;
        String urlFormat = "https://www.imdb.com/search/title/?moviemeter=%d,%d&view=simple";
        String url = String.format(urlFormat, start, end);
        return getImdbIdsAndRatingsFromTheSearchPage(url);
    }

    public LinkedHashMap<String, String> get250TitleIdsAndRatings(int pageNumber) {
        int start = (pageNumber - 1) * 250 + 1;
        int end = start + 249;
        String urlFormat = "https://www.imdb.com/search/title/?moviemeter=%d,%d&view=simple&count=250";
        String url = String.format(urlFormat, start, end);
        return getImdbIdsAndRatingsFromTheSearchPage(url);
    }

    public JsonObject getTitleData(String imdbId){
        String urlFormat = "https://www.imdb.com/title/%s/";
        String url = String.format(urlFormat, imdbId);
        return getDataFromTitlePage(url);
    }

    private static JsonObject getDataFromTitlePage(String url){
        JsonObject object = new JsonObject();
        try {
            Document doc = Jsoup.connect(url).get();
            String trailerImdbId = doc.select("a.VideoSlate__title").first().attr("href").replaceAll("/video/(.*?)/.*", "$1");
            String imdbRating = doc.select("span.cMEQkK").first().text();
            object.addProperty("trailerImdbId", trailerImdbId);
            object.addProperty("imdbRating", imdbRating);

            if (trailerImdbId.isEmpty() && imdbRating.isEmpty()){
                throw new RuntimeException("Couldn't select any ids with the given queries");
            }

        } catch (Exception e) {
            throw new RuntimeException("Couldn't fetch any data" + e);
        }
        return object;
    }

    private static List<String> getImdbIds(String url) {
        List<String> idList;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("li.ipc-metadata-list-summary-item");
            idList = items.stream().map(element -> {
                        String id = element.select("a").attr("href");
                        return id.replaceAll("/title/(.*?)/.*", "$1");
                    })
                    .collect(Collectors.toList());
            throwIfIdListIsEmpty(idList);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't fetch imdb page data" + e);
        }
        return idList;
    }

    private static LinkedHashMap<String, String> getImdbIdsAndRatings(String url) {
        LinkedHashMap<String, String> idAndRatingMap = new LinkedHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("li.ipc-metadata-list-summary-item");
            getHrefAndSpanRating(items, idAndRatingMap);
            throwIfIdAndRatingMapIsEmpty(idAndRatingMap);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't fetch imdb page data" + e);
        }
        return idAndRatingMap;
    }



    private static LinkedHashMap<String, String> getImdbIdsAndPopularity(String url) {
        LinkedHashMap<String, String> idAndPopularityMap = new LinkedHashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("li.ipc-metadata-list-summary-item");
            getHrefAndDivPopularity(items, idAndPopularityMap);
            throwIfIdAndRatingMapIsEmpty(idAndPopularityMap);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't fetch imdb page data" + e);
        }
        return idAndPopularityMap;
    }

    private static LinkedHashMap<String, String> getImdbIdsAndRatingsFromTheSearchPage(String url) {

        LinkedHashMap<String, String> idAndRatingMap = new LinkedHashMap<>();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537").get();
            Elements divItems = doc.select("div.lister-item.mode-simple");

            if (divItems.isEmpty()) {
                Elements items = doc.select("li.ipc-metadata-list-summary-item");

                getHrefAndSpanRating(items, idAndRatingMap);
            } else {
                getHrefAndDivRating(divItems, idAndRatingMap);
            }
            throwIfIdAndRatingMapIsEmpty(idAndRatingMap);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't fetch imdb page data" + e);
        }
        return idAndRatingMap;
    }

    private static void getHrefAndDivRating(Elements divItems, LinkedHashMap<String, String> idAndRatingMap) {
        divItems.forEach(element -> {

            String id = element.select("a").attr("href");
            id = id.replaceAll("/title/(.*?)/.*", "$1");

            String rating = "0";
            if (!element.select("div.col-imdb-rating > strong").text().isEmpty()) {
                rating = String.valueOf(element.select("div.col-imdb-rating > strong").text());
            }
            idAndRatingMap.putIfAbsent(id, rating);
        });
    }

    private static void getHrefAndDivPopularity(Elements items, LinkedHashMap<String, String> idAndPopularityMap) {
        items.forEach(element -> {

            String id = element.select("a.ipc-lockup-overlay").attr("href");
            id = id.replaceAll("/title/(.*?)/.*", "$1");

            String popularity = "0";
            if (!element.select("div.cli-meter-title-header").attr("aria-label").isEmpty()) {
                popularity = String.valueOf(element.select("div.cli-meter-title-header").attr("aria-label").replaceAll("Ranking ", ""));
            }
            idAndPopularityMap.putIfAbsent(id, popularity);
        });
    }

    private static void getHrefAndSpanRating(Elements items, LinkedHashMap<String, String> idAndRatingMap) {
        items.forEach(element -> {

            String id = element.select("a.ipc-lockup-overlay").attr("href");
            id = id.replaceAll("/title/(.*?)/.*", "$1");

            String rating = "0";
            if (!element.select("span.ipc-rating-star").textNodes().isEmpty()) {
                rating = String.valueOf(element.select("span.ipc-rating-star").textNodes().get(0));
            }
            idAndRatingMap.putIfAbsent(id, rating);
        });
    }

    private static void throwIfIdListIsEmpty(List<String> idList) {
        if (idList.isEmpty()) {
            throw new RuntimeException("Couldn't select any html elements with the given queries");
        }
        if (idList.stream().allMatch(String::isEmpty)) {
            throw new RuntimeException("Couldn't select any ids with the given queries");
        }
    }

    private static void throwIfIdAndRatingMapIsEmpty(LinkedHashMap<String, String> idAndRatingMap) {
        if (idAndRatingMap.isEmpty()) {
            throw new RuntimeException("Couldn't select any html elements with the given queries");
        }
        if (idAndRatingMap.keySet().stream().allMatch(String::isEmpty)) {
            throw new RuntimeException("Couldn't select any ids with the given queries");
        }
    }
}


//    public LinkedHashMap<String, String> getTitlesIdsAndRatingsFromPageNumber(int pageNumber){
//
//        int totalTitlesCount = pageNumber * 100;
//        int startTitle = (pageNumber - 1) * 100 + 1;
//        LinkedHashMap<String, String> idsAndRatings = new LinkedHashMap<>();
//        String urlFormat = "https://www.imdb.com/search/title/?moviemeter=1,%d&view=simple&count=100&start=%d&ref_=adv_nxt";
//        String url = String.format(urlFormat, totalTitlesCount, startTitle);
//        return getImdbIdsAndRatingsFromTheSearchPage(url);
//    }