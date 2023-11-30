package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.api.apidtos.ApiTrailerAddDTO;
import com.example.imdbg.model.entity.api.apidtos.ApiTrailerQualityAddDTO;
import com.example.imdbg.model.entity.movies.VideoEntity;
import com.example.imdbg.repository.movies.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    private VideoService toTest;

    @BeforeEach
    void setUp(){
        toTest = new VideoService(videoRepository);
    }

    @Test
    void createNewTrailer_returnsNull_WhenTrailerDTOisNull() {

        ApiTrailerAddDTO testDTO = null;

        VideoEntity newTrailer = toTest.createNewTrailer(testDTO);

        assertNull(newTrailer);
    }

    @Test
    void createNewTrailer_returnsNull_WhenQualityListIsNull() {

        ApiTrailerAddDTO testDTO = new ApiTrailerAddDTO();

        testDTO.setQualities(null);

        VideoEntity newTrailer = toTest.createNewTrailer(testDTO);

        assertNull(newTrailer);
    }

    @Test
    void createNewTrailer_returnsNull_WhenQualityListIsEmpty() {

        ApiTrailerAddDTO testDTO = new ApiTrailerAddDTO();

        testDTO.setQualities(new ArrayList<>());

        VideoEntity newTrailer = toTest.createNewTrailer(testDTO);

        assertNull(newTrailer);
    }

    @Test
    void createNewTrailer_returnsNull_WhenNoMatches() {

        ApiTrailerAddDTO testDTO = new ApiTrailerAddDTO();
        ApiTrailerQualityAddDTO testQuality = new ApiTrailerQualityAddDTO();
        testQuality.setVideoURL("noMatch");

        testDTO.setQualities(List.of(testQuality));

        VideoEntity newTrailer = toTest.createNewTrailer(testDTO);

        assertNull(newTrailer);
    }

    @Test
    void createNewTrailer() {

        ApiTrailerAddDTO testDTO = new ApiTrailerAddDTO();
        ApiTrailerQualityAddDTO testQuality = new ApiTrailerQualityAddDTO();
        testQuality.setVideoURL("https://www.imdb.com/video/vi33474329/");

        testDTO.setQualities(List.of(testQuality));

        VideoEntity newTrailer = toTest.createNewTrailer(testDTO);

        assertEquals(newTrailer.getVideoImdbId(), "vi33474329");
    }
}