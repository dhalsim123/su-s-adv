package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.example.imdbg.model.entity.movies.PhotoEntity;
import com.example.imdbg.repository.movies.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    private PhotoService toTest;

    @BeforeEach
    void setUp(){
        toTest = new PhotoService(photoRepository);
    }

    @Test
    void createNewMainPosterFromApiDataDTO() {
        ApiMovieAddDTO testDTO = new ApiMovieAddDTO();

        String testPosterUrl = "testUrl";
        testDTO.setUrlPoster(testPosterUrl);

        PhotoEntity result = toTest.createNewMainPosterFromApiDataDTO(testDTO);

        assertEquals(result.getPhotoUrl(), testPosterUrl);
        assertTrue(result.isPoster());
    }
}