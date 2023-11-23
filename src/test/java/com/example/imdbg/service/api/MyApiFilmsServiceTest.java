package com.example.imdbg.service.api;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MyApiFilmsServiceTest {

    @Autowired
    private MyApiFilmsService toTest;

    @Test
    void requestMovieDataForImdbIdAsDTO() {
        String testImdbId = "tt0167260";

        ApiMovieAddDTO apiMovieAddDTO = toTest.requestMovieDataAsDTOForImdbId(testImdbId);

        Assertions.assertEquals(apiMovieAddDTO.getIdIMDB(), testImdbId);
    }

    @Test
    void requestMovieDataForImdbIdAsJson() {
        String testImdbId = "tt0167260";

        JsonElement movieJson = toTest.requestMovieDataAsJsonForImdbId(testImdbId);

        Assertions.assertTrue(movieJson.toString().contains("tt0167260"));
    }

    @Test
    void requestMovieDataForImdbIdAsDTO_Error() {
        String testImdbId = "123";

        Assertions.assertThrows(Exception.class, () -> toTest.requestMovieDataAsDTOForImdbId(testImdbId));


    }
}