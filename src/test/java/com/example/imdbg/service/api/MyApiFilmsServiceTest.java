package com.example.imdbg.service.api;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MyApiFilmsServiceTest {

    @Autowired
    private MyApiFilmsService toTest;
    @Test
    void requestMovieDataForImdbId() {
        String testImdbId = "tt0167260";

        ApiMovieAddDTO apiMovieAddDTO = toTest.requestMovieDataForImdbId(testImdbId);


    }

    @Test
    void test1() {
    }
}