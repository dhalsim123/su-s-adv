package com.example.imdbg.model.entity.api.apidtos;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiMovieAddDTO {

    @SerializedName("idIMDB")
    private String idIMDB;
    @SerializedName("year")
    @Getter(AccessLevel.NONE)
    private String year;
    @SerializedName("releaseDate")
    private String releaseDate;

    @SerializedName("actors")
    private List<ApiPersonAddDTO> actors;

    @SerializedName("directors")
    private List<ApiPersonAddDTO> directors;

    @SerializedName("writers")
    private List<ApiPersonAddDTO> writers;

    @SerializedName("runtime")
    @Getter(AccessLevel.NONE)
    private String runtime;
    @SerializedName("genres")
    @Getter(AccessLevel.NONE)
    private List<String> genres;
    @SerializedName("plot")
    private String plot;
    @SerializedName("simplePlot")
    private String simplePlot;


    @SerializedName("rating")
    @Getter(AccessLevel.NONE)
    private String rating;

    private String imdbTop250Rank;
    private String popularity;


    @SerializedName("metascore")
    @Getter(AccessLevel.NONE)
    private String metascore;

    @SerializedName("votes")
    @Getter(AccessLevel.NONE)
    private String votes;

    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("urlPoster")
    private String urlPoster;

    private ApiTrailerAddDTO trailer;

    private ApiBoxOfficeAddDTO business;


    public String getRating() {
        return rating == null ? "0" : rating;
    }

    public String getMetascore() {
        return metascore == null ? "0" : metascore;
    }

    public String getVotes() {
        return votes == null ? "0" : votes;
    }

    public String getRuntime() {
        return runtime == null ? "0" : runtime;
    }

    public String getYear() {
        return year == null ? "0" : year;
    }

    public List<ApiPersonAddDTO> getActors() {
        return actors == null ? new ArrayList<>() : actors;
    }

    public List<ApiPersonAddDTO> getDirectors() {
        return directors == null ? new ArrayList<>() : directors;
    }

    public List<ApiPersonAddDTO> getWriters() {
        return writers == null ? new ArrayList<>() : writers;
    }

    public List<String> getGenres() {
        if (genres != null){
            if (genres.contains("Sci-Fi")){
                genres.remove("Sci-Fi");
                genres.add("SCI_FI");
            }
            if (genres.contains("Film-Noir")){
                genres.remove("Film-Noir");
                genres.add("FILM_NOIR");
            }
        }
        return genres;
    }
}
