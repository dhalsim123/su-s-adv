package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.example.imdbg.model.entity.movies.PhotoEntity;
import com.example.imdbg.repository.movies.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public PhotoEntity createNewMainPosterFromApiDataDTO (ApiMovieAddDTO dto){
        return PhotoEntity.builder()
                .photoUrl(dto.getUrlPoster())
                .isPoster(true).build();
    }
}
