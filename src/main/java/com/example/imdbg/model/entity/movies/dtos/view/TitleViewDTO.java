package com.example.imdbg.model.entity.movies.dtos.view;

import com.example.imdbg.model.entity.movies.dtos.view.characterRole.CharacterRoleViewDTO;
import com.example.imdbg.model.entity.movies.dtos.view.person.PersonViewDTO;
import com.example.imdbg.model.entity.movies.dtos.view.type.TypeViewDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TitleViewDTO {

    private Long id;
    private String title;
    private Integer year;
    private Long runtime;
    private float imdbRating;
    private Integer imdbTop250Rank;
    private Integer metascore;
    private String mainPosterURLPhotoUrl;
    private String mainTrailerURLVideoImdbId;
    private String simplePlot;
    private String plot;
    private LocalDate releaseDate;
    private TypeViewDTO type;
    private Integer popularity;
    private List<GenreViewDTO> genres;

    private String boxOfficeOpeningWeekend;
    private String boxOfficeGrossUsa;
    private String boxOfficeWorldwide;

    private List<PersonViewDTO> actors;
    private List<PersonViewDTO> directors;
    private List<PersonViewDTO> writers;

    private List<CharacterRoleViewDTO> characterRoles;

    public String getDurationPrintFormat() {

        Long totalDuration = this.runtime;

        long hours = totalDuration / 3600;
        long minutes = (totalDuration % 3600) / 60;
        long seconds = totalDuration % 60;

        return hours == 0 ? String.format("%d h %02dm", minutes, seconds) : String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getPersonPhotoUrl (Long id){
        List<String> list = this.actors.stream().filter(actor -> actor.getId().equals(id))
                .map(PersonViewDTO::getPhotoUrl)
                .limit(1).toList();

        if (!list.isEmpty()){
            return list.get(0) != null ? list.get(0) : "https://png.pngtree.com/png-clipart/20210608/ourmid/pngtree-dark-gray-simple-avatar-png-image_3418404.jpg";
        }
        return "https://png.pngtree.com/png-clipart/20210608/ourmid/pngtree-dark-gray-simple-avatar-png-image_3418404.jpg";
    }

    public boolean boxOfficeIsEmpty(){
        return this.boxOfficeGrossUsa == null && this.boxOfficeWorldwide == null && this.boxOfficeOpeningWeekend == null;
    }

}
