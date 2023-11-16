package com.example.imdbg.model.exceptions;

public class TitleCreationException extends RuntimeException{
    public TitleCreationException(String imdbId, Exception e) {

        super("Error creating title from ApiMovieAddDTO " + e);
    }
}
