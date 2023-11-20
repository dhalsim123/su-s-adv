package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.example.imdbg.model.entity.movies.CharacterRoleEntity;
import com.example.imdbg.model.entity.movies.PersonEntity;
import com.example.imdbg.model.entity.movies.PhotoEntity;
import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.repository.movies.CharacterRoleRepository;
import com.example.imdbg.repository.movies.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

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