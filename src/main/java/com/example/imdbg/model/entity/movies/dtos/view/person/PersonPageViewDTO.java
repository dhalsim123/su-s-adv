package com.example.imdbg.model.entity.movies.dtos.view.person;

import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonPageViewDTO {

    private Long id;

    private String name;

    @Getter(AccessLevel.NONE)
    private String photoUrl;

    private List<TitleViewDTO> actedInTitles;

    private List<TitleViewDTO> directedTitles;

    private List<TitleViewDTO> writtenTitles;

    private String bestRatedTitleVideoImdb;

    private List<TitleViewDTO> knownFor;

    private List<TitleViewDTO> actingInUpcomingTitles;
    private List<TitleViewDTO> directingUpcomingTitles;
    private List<TitleViewDTO> writingForUpcomingTitles;

    public String getPhotoUrl(){
        if (this.photoUrl != null){
            return this.photoUrl.replaceAll(photoUrl.substring(photoUrl.indexOf("._V1"), photoUrl.lastIndexOf(".")), "");
        }
        return "https://png.pngtree.com/png-clipart/20210608/ourmid/pngtree-dark-gray-simple-avatar-png-image_3418404.jpg";
    }

    public void setBestRatedTitleVideoImdbAndKnownFor(){
        if (!this.actedInTitles.isEmpty()){
            List<TitleViewDTO> list = actedInTitles.stream().sorted(Comparator.comparing(TitleViewDTO::getImdbRating)).toList();
            this.knownFor = list.stream().limit(4).toList();
            this.bestRatedTitleVideoImdb = list.get(0).getMainTrailerURLVideoImdbId();
        }
        else if (!this.directedTitles.isEmpty()){
            List<TitleViewDTO> list = directedTitles.stream().sorted(Comparator.comparing(TitleViewDTO::getImdbRating)).toList();
            this.knownFor = list.stream().limit(4).toList();
            this.bestRatedTitleVideoImdb = list.get(0).getMainTrailerURLVideoImdbId();
        }
        else if (!this.writtenTitles.isEmpty()){
            List<TitleViewDTO> list = writtenTitles.stream().sorted(Comparator.comparing(TitleViewDTO::getImdbRating)).toList();
            this.knownFor = list.stream().limit(4).toList();
            this.bestRatedTitleVideoImdb = list.get(0).getMainTrailerURLVideoImdbId();
        }
    }

    public void setTitles(){
        this.actingInUpcomingTitles = this.actedInTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() == null || titleViewDTO.getReleaseDate().isAfter(LocalDate.now()))
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();

        this.actedInTitles = this.actedInTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() != null && titleViewDTO.getReleaseDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(TitleViewDTO::getReleaseDate).reversed())
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();

        this.directingUpcomingTitles = this.directedTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() == null || titleViewDTO.getReleaseDate().isAfter(LocalDate.now()))
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();

        this.directedTitles = this.directedTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() != null && titleViewDTO.getReleaseDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(TitleViewDTO::getReleaseDate).reversed())
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();

        this.writingForUpcomingTitles = this.writtenTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() == null || titleViewDTO.getReleaseDate().isAfter(LocalDate.now()))
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();

        this.writtenTitles = this.writtenTitles.stream().filter(titleViewDTO -> titleViewDTO.getReleaseDate() != null && titleViewDTO.getReleaseDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(TitleViewDTO::getReleaseDate).reversed())
                .peek(titleViewDTO -> titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414")))
                .toList();


    }

}
