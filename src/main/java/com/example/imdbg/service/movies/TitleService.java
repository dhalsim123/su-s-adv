package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.*;
import com.example.imdbg.model.entity.movies.apidtos.ApiMovieAddDTO;
import com.example.imdbg.model.entity.movies.apidtos.ApiPersonAddDTO;
import com.example.imdbg.model.entity.movies.dtos.view.TitleCarouselViewDTO;
import com.example.imdbg.model.entity.movies.dtos.view.TitleSearchViewDTO;
import com.example.imdbg.model.entity.movies.dtos.view.TitleVideoViewDTO;
import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import com.example.imdbg.model.entity.movies.enums.TypeEnum;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.model.exceptions.TitleCreationException;
import com.example.imdbg.repository.movies.TitleRepository;
import com.example.imdbg.service.api.MyApiFilmsService;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    private final GenreService genreService;

    private final TypeService typeService;

    private final PersonService personService;

    private final PhotoService photoService;

    private final VideoService videoService;
    private final CharacterRoleService characterRoleService;

    private final ImdbScrapeService imdbScrapeService;
    private final MyApiFilmsService myApiFilmsService;

    private final ModelMapper modelMapper;

    private final Logger LOGGER = LoggerFactory.getLogger(TitleService.class);

    private List<String> threadLog = new ArrayList<>();

    public TitleService(TitleRepository titleRepository, GenreService genreService, TypeService typeService, PersonService personService, PhotoService photoService, VideoService videoService, CharacterRoleService characterRoleService, ImdbScrapeService imdbScrapeService, MyApiFilmsService myApiFilmsService, ModelMapper modelMapper) {
        this.titleRepository = titleRepository;
        this.genreService = genreService;
        this.typeService = typeService;
        this.personService = personService;
        this.photoService = photoService;
        this.videoService = videoService;
        this.characterRoleService = characterRoleService;
        this.imdbScrapeService = imdbScrapeService;
        this.myApiFilmsService = myApiFilmsService;
        this.modelMapper = modelMapper;
    }

    public void fetchTop250ImdbTitles(){
        try {
            LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();
            String info = createNewTitlesWithIdsAndRatingsMap(top250IdsAndRatings);
            LOGGER.info("Finished fetching the top 250 Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch top250ImdbTitles because of this error:" + e);
        }
    }
    public void fetch100MostPopularImdbTitles(){
        try {
            LinkedHashMap<String, String> mostPopularIdsAndRatings = imdbScrapeService.get100MostPopularIdsAndRatings();
            String info = createNewTitlesWithIdsAndRatingsMap(mostPopularIdsAndRatings);
            LOGGER.info("Finished fetching the 100 most popular Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    public void fetchUpcomingImdbTitles(){
        try {
            List<String> upcomingReleases = imdbScrapeService.getUpcomingReleases();
            String info = createNewTitlesWithIdsList(upcomingReleases);
            LOGGER.info("Finished fetching the most popular upcoming releases of Imdb titles. " + info);
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch UpcomingImdbTitles because of this error: " + e);
        }
    }

    public void fetchSingleImdbTitle(String imdbId){
        try {
            createNewTitlesWithIdsList(new ArrayList<>(List.of(imdbId)));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch this Imdb title " + imdbId + " because of this error:" + e);
        }
    }

    public List<String> getThreadLog(){
        return this.threadLog;
    }

    public void clearThreadLog(){
        this.threadLog = new ArrayList<>();
    }

    public void fetch50Titles(int pageNumber){
        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get50TitleIdsAndRatings(pageNumber);
            String anyFailedIds = createNewTitlesWithIdsAndRatingsMap(idsAndRatingsMap);
            LOGGER.info("Finished fetching page " + pageNumber + "  of the Imdb titles search results. " + (anyFailedIds.isEmpty() ? "" : "But failed to save: " + anyFailedIds));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }

    public void fetch250Titles(int pageNumber){
        try {
            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.get250TitleIdsAndRatings(pageNumber);
            String anyFailedIds = createNewTitlesWithIdsAndRatingsMap(idsAndRatingsMap);
            LOGGER.info("Finished fetching the " + pageNumber + " page of Imdb titles. " + (anyFailedIds.isEmpty() ? "" : "But failed to save: " + anyFailedIds));
        }
        catch (Exception e){
            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
        }
    }


//    public void fetchTitlesFromPageNumber(int pageNumber){
//        try {
//            LinkedHashMap<String, String> idsAndRatingsMap = imdbScrapeService.getTitlesIdsAndRatingsFromPageNumber(pageNumber);
//            String anyFailedIds = createNewTitlesWithIdsAndRatingsMap(idsAndRatingsMap);
//            LOGGER.info("Finished fetching the " + pageNumber + " page of Imdb titles. " + (anyFailedIds.isEmpty() ? "" : "But failed to save: " + anyFailedIds));
//        }
//        catch (Exception e){
//            LOGGER.error("Couldn't fetch 100MostPopularImdbTitles because of this error: " + e);
//        }
//    }

    public void tryToUpdateTitlesReleasedPastMonth(){
        LocalDate releasedPastMonth = LocalDate.now().minusMonths(1);
        LocalDate lastUpdatedBeforeTwoWeeks = LocalDate.now().minusWeeks(2);

        List<TitleEntity> titlesToUpdate = titleRepository.findAllByReleaseDateAfterAndLastUpdatedBefore(releasedPastMonth, lastUpdatedBeforeTwoWeeks);

        titlesToUpdate.forEach(title -> {
            ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataForImdbId(title.getImdbId());
            updateTitle(title, apiMovieAddDTO);
        });
    }



    private String createNewTitlesWithIdsAndRatingsMap(LinkedHashMap<String, String> idsAndRatingsMap) {
        List<String> idsList = new ArrayList<>(idsAndRatingsMap.keySet());
        List<String> filteredList = filterExistingImdbIdTitles(idsList);

        List<String> failedToSaveIds = new ArrayList<>();

        filteredList.forEach(titleImdbId ->{
            try {
                ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataForImdbId(titleImdbId);
                apiMovieAddDTO.setRating(idsAndRatingsMap.get(titleImdbId));
                this.createTitleFromApiDataDTO(apiMovieAddDTO);
            }
            catch (Exception e){
                LOGGER.error("Couldn't save title with Imdb Id " + titleImdbId + " " + e);
                failedToSaveIds.add(titleImdbId);
            }
        });
        
        return getResultToString(filteredList, failedToSaveIds);
    }

    private String createNewTitlesWithIdsList(List<String> idsList) {
        List<String> filteredList = filterExistingImdbIdTitles(idsList);

        List<String> failedToSaveIds = new ArrayList<>();

        filteredList.forEach(titleImdbId ->{
            try {
                ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataForImdbId(titleImdbId);
                this.createTitleFromApiDataDTO(apiMovieAddDTO);
            }
            catch (Exception e){
                LOGGER.error("Couldn't save title with Imdb Id " + titleImdbId + " " + e);
                failedToSaveIds.add(titleImdbId);
            }
        });

        return getResultToString(filteredList, failedToSaveIds);
    }

    private String getResultToString(List<String> filteredList, List<String> failedToSaveIds){

        if (filteredList.isEmpty() && failedToSaveIds.isEmpty()){
            String s = "Already in DB";
            threadLog.add(s);
            return s;
        }


        if (filteredList.size() == failedToSaveIds.size()){
            String s = "Failed to save any Ids. Check the back-end logs for more info";
            threadLog.add(s);
            return "Failed to save any Ids. Check logs for more info";
        }


        filteredList.removeAll(failedToSaveIds);

        String created = "Created: " + String.join(", ", filteredList);
        String failedToSave = "Failed to save: " + String.join(", ", failedToSaveIds);
        threadLog.add(created);
        threadLog.add(failedToSave);

        return failedToSave;
    }

    private void createTitleFromApiDataDTO(ApiMovieAddDTO apiMovieAddDTO){
        try {
            if (apiMovieAddDTO == null) {
                throw new IllegalArgumentException("ApiMovieAddDTO cannot be null");
            }

            List<GenreEntity> genres = this.getGenreEntities(apiMovieAddDTO);

            TypeEntity type = this.getTypeEntity(apiMovieAddDTO);

            LinkedHashMap<String, PersonEntity> peoplesMap = personService.findPersonEntitiesFromApiPersonDTOs(apiMovieAddDTO.getActors(), apiMovieAddDTO.getDirectors(), apiMovieAddDTO.getWriters());

            List<PersonEntity> actors = this.getPersonEntities(apiMovieAddDTO.getActors(), peoplesMap);
            List<PersonEntity> directors = this.getPersonEntities(apiMovieAddDTO.getDirectors(), peoplesMap);
            List<PersonEntity> writers = this.getPersonEntities(apiMovieAddDTO.getWriters(), peoplesMap);

            TitleEntity newTitle = TitleEntity.builder()
                    .imdbId(apiMovieAddDTO.getIdIMDB())
                    .title(apiMovieAddDTO.getTitle())
                    .releaseDate(parseReleaseDate(apiMovieAddDTO.getReleaseDate()))
                    .year(Integer.valueOf(apiMovieAddDTO.getYear()))
                    .plot(apiMovieAddDTO.getPlot())
                    .simplePlot(apiMovieAddDTO.getSimplePlot())
                    .imdbRating(Float.parseFloat(apiMovieAddDTO.getRating()))
                    .metascore(Integer.valueOf(apiMovieAddDTO.getMetascore()))
                    .runtime(Long.valueOf(apiMovieAddDTO.getRuntime()))
                    .genres(genres)
                    .type(type)
                    .actors(actors)
                    .directors(directors)
                    .writers(writers)
                    .mainPosterURL(photoService.createNewMainPosterFromApiDataDTO(apiMovieAddDTO))
                    .mainTrailerURL(apiMovieAddDTO.getTrailer() == null ? null : videoService.createNewTrailer(apiMovieAddDTO.getTrailer()))
                    .boxOfficeOpeningWeekend(apiMovieAddDTO.getBusiness() == null ? null : apiMovieAddDTO.getBusiness().getOpeningWeekend())
                    .boxOfficeGrossUsa(apiMovieAddDTO.getBusiness() == null ? null : apiMovieAddDTO.getBusiness().getGrossUsa())
                    .boxOfficeWorldwide(apiMovieAddDTO.getBusiness() == null ? null : apiMovieAddDTO.getBusiness().getWorldwide())
                    .lastUpdated(LocalDate.now())
                    .build();

            titleRepository.saveAndFlush(newTitle);

            this.createCharacterRoles(apiMovieAddDTO);

            LOGGER.info("Created new title: " + apiMovieAddDTO.getIdIMDB() + " " + apiMovieAddDTO.getTitle());
        }
        catch (Exception e){
            LOGGER.error("Error creating title from ApiMovieAddDTO", e);
            throw new TitleCreationException(apiMovieAddDTO != null ? apiMovieAddDTO.getIdIMDB() : null, e);
        }
    }

    public List<TitleEntity> findTop250ImdbRatedTitles(){
        return titleRepository.findTop250ImdbRatedTitles();
    }

    public List<TitleEntity> findTop250ImdbRatedMovies(){
        return titleRepository.findTop250ImdbRatedMovies();
    }

    public List<TitleEntity> findMostPopularImdbRatedMovies(){
        return titleRepository.findAllByType_NameAndMainTrailerURLNotNullAndPopularityNotNullOrderByPopularityAsc(TypeEnum.MOVIE);
    }

    public TitleEntity findTitleEntityByImdbId(String imdbId){
        return titleRepository.findTitleEntityByImdbId(imdbId).orElseThrow(() -> new ObjectNotFoundException("Title with IMDB id " + imdbId + " was not found"));
    }

    private TypeEntity getTypeEntity(ApiMovieAddDTO apiMovieAddDTO) {
        if (apiMovieAddDTO.getType() != null) {
            return typeService.findTypeEntityByName(apiMovieAddDTO.getType());
        }
        return null;
    }

    private List<GenreEntity> getGenreEntities(ApiMovieAddDTO apiMovieAddDTO) {
        List<GenreEntity> genres = new ArrayList<>();
        if (apiMovieAddDTO.getGenres() != null) {
            genres = genreService.findGenreEntitiesByName(apiMovieAddDTO.getGenres());
        }
        return genres;
    }

    private List<PersonEntity> getPersonEntities(List<ApiPersonAddDTO> personAddDTOS, LinkedHashMap<String, PersonEntity> peoplesMap) {
        List<PersonEntity> personEntities = new ArrayList<>();
        personAddDTOS.forEach(dto -> personEntities.add(peoplesMap.get(dto.getId())));
        return personEntities;
    }

    private LocalDate parseReleaseDate(String releaseDate){

        if (releaseDate == null){
            return null;
        }

        if (releaseDate.isEmpty()){
            return null;
        }

        try {
            return LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        catch (Exception e){
            LOGGER.error("Date format error");
            throw new IllegalArgumentException("Date format error", e);
        }
    }

    private void createCharacterRoles(ApiMovieAddDTO apiMovieAddDTO) {
        apiMovieAddDTO.getActors().forEach(apiActorAddDTO -> {
            characterRoleService.createCharacterRole(
                    this.findTitleEntityByImdbId(apiMovieAddDTO.getIdIMDB()),
                    personService.findPersonEntityByImdbId(apiActorAddDTO.getId()),
                    apiActorAddDTO.getCharacter());
        });
    }

    private List<String> filterExistingImdbIdTitles(List<String> list) {
        List<String> newList = new ArrayList<>(List.copyOf(list));
        List<TitleEntity> existingTitles = titleRepository.findAllByImdbIdIsIn(list);

        List<String> existingImdbIds = existingTitles.stream().map(TitleEntity::getImdbId).toList();

        newList.removeAll(existingImdbIds);

        return newList;
    }

    private void updateTitle(TitleEntity title, ApiMovieAddDTO apiMovieAddDTO) {
        try {
            if (apiMovieAddDTO.getBusiness() != null){
                title.setBoxOfficeGrossUsa(apiMovieAddDTO.getBusiness().getGrossUsa());
                title.setBoxOfficeWorldwide(apiMovieAddDTO.getBusiness().getWorldwide());
                title.setBoxOfficeOpeningWeekend(apiMovieAddDTO.getBusiness().getOpeningWeekend());
            }


            List<GenreEntity> genreEntities = this.getGenreEntities(apiMovieAddDTO);
            title.setGenres(genreEntities);


            if (title.getMainTrailerURL() == null){
                VideoEntity videoEntity = apiMovieAddDTO.getTrailer() == null ? null : videoService.createNewTrailer(apiMovieAddDTO.getTrailer());
                title.setMainTrailerURL(videoEntity);
            }

            if (title.getMainPosterURL() == null){
                title.setMainPosterURL(photoService.createNewMainPosterFromApiDataDTO(apiMovieAddDTO));
            }

            LinkedHashMap<String, PersonEntity> peoplesMap = personService.findPersonEntitiesFromApiPersonDTOs(apiMovieAddDTO.getActors(), apiMovieAddDTO.getDirectors(), apiMovieAddDTO.getWriters());

            List<PersonEntity> actors = this.getPersonEntities(apiMovieAddDTO.getActors(), peoplesMap);
            List<PersonEntity> directors = this.getPersonEntities(apiMovieAddDTO.getDirectors(), peoplesMap);
            List<PersonEntity> writers = this.getPersonEntities(apiMovieAddDTO.getWriters(), peoplesMap);

            title.setActors(actors);
            title.setDirectors(directors);
            title.setWriters(writers);

            characterRoleService.deleteAllCharacterRolesForTitle(title);
            this.createCharacterRoles(apiMovieAddDTO);

            title.setRuntime(Long.valueOf(apiMovieAddDTO.getRuntime()));

            titleRepository.saveAndFlush(title);
        }
        catch (Exception e){
            LOGGER.error("Couldn't update this title " + title.getImdbId() + " because of this error " + e);
        }
    }

    public List<TitleSearchViewDTO> getSearchSuggestionsContaining(String search){
        List<TitleEntity> first7ByTitleContaining = titleRepository.findFirst7ByTitleContaining(search);

        return mapTitleSearchViewDTOS(first7ByTitleContaining);
    }

    private List<TitleSearchViewDTO> mapTitleSearchViewDTOS(List<TitleEntity> list) {
        return list
                .stream()
                .map(title -> {
                    TitleSearchViewDTO dto = modelMapper.map(title, TitleSearchViewDTO.class);
                    dto.setMainPosterURLPhotoUrl(dto.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UY148_CR3,0,100,148_"));
                    return dto;
                })
                .toList();
    }

    public TitleEntity findTitleById(Long id){
        return titleRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Title with id " + id + " was not found"));
    }
    public List<TitleCarouselViewDTO> get18TopRatedCarouselViewDTOs (){
        List<TitleEntity> top250ImdbRatedTitles = this.findTop250ImdbRatedMovies().stream().limit(18).toList();

        return mapTitleCarouselViewDTOS(top250ImdbRatedTitles);
    }

    public List<TitleCarouselViewDTO> get4MostPopularCarouselViewDTOs (){
        List<TitleEntity> mostPopular4 = this.findMostPopularImdbRatedMovies().stream().limit(4).toList();

        return mapTitleCarouselViewDTOS(mostPopular4);
    }

    private List<TitleCarouselViewDTO> mapTitleCarouselViewDTOS(List<TitleEntity> list) {
        return list.stream().map(title -> {
            TitleCarouselViewDTO map = modelMapper.map(title, TitleCarouselViewDTO.class);
            map.setMainPosterURLPhotoUrl(map.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414"));
            return map;
        }).toList();
    }

    public void updateImdb100MostPopular(){
        LinkedHashMap<String, String> mostPopularIdsAndPopularity = imdbScrapeService.get100MostPopularIdsAndPopularity();

        ArrayList<String> idsList = new ArrayList<>(mostPopularIdsAndPopularity.keySet());

        clearImdbPopularity();

        List<String> newTitleIds = this.filterExistingImdbIdTitles(idsList);

        createNewTitlesWithIdsList(newTitleIds);

        List<TitleEntity> allByImdbIdIsIn = titleRepository.findAllByImdbIdIsIn(idsList);

        allByImdbIdIsIn.forEach(title -> title.setPopularity(Integer.valueOf(mostPopularIdsAndPopularity.get(title.getImdbId()))));

        titleRepository.saveAllAndFlush(allByImdbIdIsIn);

    }

    public void updateImdbTop250(){
        LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();

        ArrayList<String> idsList = new ArrayList<>(top250IdsAndRatings.keySet());

        List<String> newTitleIds = this.filterExistingImdbIdTitles(idsList);

        createNewTitlesWithIdsList(newTitleIds);

        List<TitleEntity> allByImdbIdIsIn = titleRepository.findAllByImdbIdIsIn(idsList);

        allByImdbIdIsIn.forEach(title -> title.setImdbRating(Float.parseFloat(top250IdsAndRatings.get(title.getImdbId()))));

        titleRepository.saveAllAndFlush(allByImdbIdIsIn);

    }


    private void clearImdbPopularity(){

        List<TitleEntity> allByPopularityNotNull = titleRepository.findAllByPopularityNotNull();

        allByPopularityNotNull.forEach(title -> title.setPopularity(null));

        titleRepository.saveAllAndFlush(allByPopularityNotNull);
    }

    @Transactional
    public TitleVideoViewDTO getTitleVideoViewDTOByVideoImdbId (String videoImdbId){

        TitleEntity title = findTitleByVideoImdbId(videoImdbId);

        return modelMapper.map(title, TitleVideoViewDTO.class);
    }

    private TitleEntity findTitleByVideoImdbId(String videoImdbId) {
        return titleRepository.findTitleEntityByMainTrailerURL_VideoImdbId(videoImdbId).orElseThrow(() -> new ObjectNotFoundException("Video with id " + videoImdbId + " was not found"));
    }

    @Transactional
    public TitleViewDTO getTitleViewDTOById (Long id){
        TitleEntity titleById = this.findTitleById(id);
        return modelMapper.map(titleById, TitleViewDTO.class);
    }

    @Transactional
    public List<TitleViewDTO> getTop250TitleViewDTOs (){
        List<TitleEntity> top250ImdbRatedMovies = this.findTop250ImdbRatedMovies();
        return mapTitleViewDTOS(top250ImdbRatedMovies);
    }

    @Transactional
    public List<TitleViewDTO> get100MostPopularTitleViewDTOs (){
        List<TitleEntity> mostPopularImdbRatedMovies = this.findMostPopularImdbRatedMovies();
        return mapTitleViewDTOS(mostPopularImdbRatedMovies);
    }

    public List<TitleViewDTO> mapTitleViewDTOS(List<TitleEntity> titles) {
        return titles
                .stream()
                .map(title -> {
                    TitleViewDTO titleViewDTO = modelMapper.map(title, TitleViewDTO.class);
                    titleViewDTO.setMainPosterURLPhotoUrl(titleViewDTO.getMainPosterURLPhotoUrl().replace("._V1", "._V1_QL75_UX280_CR0,3,280,414"));
                    return titleViewDTO;
                })
                .toList();
    }

    public List<TitleViewDTO> getTitleViewDTOsFromIdsList(List<Long> idsList){
        List<TitleEntity> allById = titleRepository.findAllById(idsList);

        return mapTitleViewDTOS(allById);
    }

    public void incrementPageViews(Long titleId, Integer count){
        try {
            TitleEntity titleById = this.findTitleById(titleId);
            titleById.setPageViews(titleById.getPageViews() == null ? count : titleById.getPageViews() + count);
            titleRepository.saveAndFlush(titleById);
        }
        catch (Exception e){
            LOGGER.error("Couldn't increment page views for titleId {} because of this error {}", titleId, e.getMessage());
        }
    }

    //public void updateEmptyTop250ImdbRatings(){
    //        LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();
    //
    //        List<String> currentTop250ImdbIds = new ArrayList<>(top250IdsAndRatings.keySet());
    //
    //        List<TitleEntity> idsWithNoRating = titleRepository.findAllByImdbIdIsInAndImdbRating(currentTop250ImdbIds, 0);
    //
    //        idsWithNoRating.forEach(title -> {
    //            title.setImdbRating(Float.parseFloat(top250IdsAndRatings.get(title.getImdbId())));
    //            titleRepository.saveAndFlush(title);
    //        });
    //    }
}
