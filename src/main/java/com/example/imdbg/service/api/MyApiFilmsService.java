package com.example.imdbg.service.api;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MyApiFilmsService {

    private final ImdbScrapeService imdbScrapeService;

    private final WebClient webClient;
    private final Gson gson;

    public MyApiFilmsService(ImdbScrapeService imdbScrapeService, WebClient webClient, Gson gson) {
        this.imdbScrapeService = imdbScrapeService;
        this.webClient = webClient;
        this.gson = gson;
    }

    public ApiMovieAddDTO requestMovieDataForImdbId(String titleImdbId){
        String API_REQUEST_FORMAT = "https://www.myapifilms.com/imdb/idIMDB?idIMDB=%s&token=398c275c-acc7-4d0a-a561-931c5c39d99c&format=json&language=en-us&aka=0&business=1&seasons=1&seasonYear=1&technical=0&trailers=1&movieTrivia=0&awards=0&moviePhotos=0&movieVideos=0&actors=1&biography=0&bornDied=0&actorActress=1&similarMovies=0&goofs=0&keyword=0&quotes=0&fullSize=1&companyCredits=0&filmingLocations=0&directors=1&writers=1";
        String urlApi = String.format(API_REQUEST_FORMAT, titleImdbId);

        String response = webClient.get()
                .uri(urlApi)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonObject data = gson.fromJson(response, JsonObject.class);

        if (data.has("error")){
            JsonObject error = data.getAsJsonObject("error");
            int errorCode = error.get("code").getAsInt();
            String errorMessage = error.get("message").getAsString();
            throw new RuntimeException("MyApiFilms error code: " + errorCode + " Message: " + errorMessage);
        }

        JsonArray moviesArray = data.getAsJsonObject("data").getAsJsonArray("movies");

        return gson.fromJson(moviesArray.get(0), ApiMovieAddDTO.class);
    }
}
