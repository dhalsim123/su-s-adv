package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.api.apidtos.ApiTrailerAddDTO;
import com.example.imdbg.model.entity.movies.VideoEntity;
import com.example.imdbg.repository.movies.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public VideoEntity createNewTrailer(ApiTrailerAddDTO dto){


        if (dto != null && dto.getQualities() != null && !dto.getQualities().isEmpty()) {

            String regex = "(vi\\d+)";

            String videoURL = dto.getQualities().get(0).getVideoURL();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(videoURL);

            String videoId = null;

            if (matcher.find()) {
                videoId = matcher.group(1);
            }
            else {
                return null;
            }

            return VideoEntity.builder()
                            .videoImdbId(videoId)
                            .description(dto.getDescription())
                            .isTrailer(true)
                            .build();
        }
        else return null;
    }

    public VideoEntity createNewTrailerFromIdOnly(String videoImdbId){

        if (videoImdbId != null && !videoImdbId.isEmpty()){
            return VideoEntity.builder()
                    .videoImdbId(videoImdbId)
                    .isTrailer(true)
                    .build();
        }

       return null;
    }
}
