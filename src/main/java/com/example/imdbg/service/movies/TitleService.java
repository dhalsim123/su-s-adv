package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.*;
import com.example.imdbg.model.entity.api.apidtos.ApiMovieAddDTO;
import com.example.imdbg.model.entity.api.apidtos.ApiPersonAddDTO;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final DataSource dataSource;

    private final ModelMapper modelMapper;
    private final Gson gson;

    private final Logger LOGGER = LoggerFactory.getLogger(TitleService.class);


    public TitleService(TitleRepository titleRepository, GenreService genreService, TypeService typeService, PersonService personService, PhotoService photoService, VideoService videoService, CharacterRoleService characterRoleService, ImdbScrapeService imdbScrapeService, MyApiFilmsService myApiFilmsService, DataSource dataSource, ModelMapper modelMapper, Gson gson) {
        this.titleRepository = titleRepository;
        this.genreService = genreService;
        this.typeService = typeService;
        this.personService = personService;
        this.photoService = photoService;
        this.videoService = videoService;
        this.characterRoleService = characterRoleService;
        this.imdbScrapeService = imdbScrapeService;
        this.myApiFilmsService = myApiFilmsService;
        this.dataSource = dataSource;
        this.modelMapper = modelMapper;
        this.gson = gson;
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

//    public void tryToUpdateTitlesReleasedPastMonth() {
//        LocalDate releasedPastMonth = LocalDate.now().minusMonths(1);
//        LocalDate lastUpdatedBeforeTwoWeeks = LocalDate.now().minusWeeks(2);
//
//        List<TitleEntity> titlesToUpdate = titleRepository.findAllByReleaseDateAfterAndLastUpdatedBefore(releasedPastMonth, lastUpdatedBeforeTwoWeeks);
//
//        titlesToUpdate.forEach(title -> {
//            ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataAsDTOForImdbId(title.getImdbId());
//            updateTitle(title, apiMovieAddDTO);
//        });
//    }

    public void writeAJsonForTop250Titles() throws IOException {
        LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();
        UUID uuid = UUID.randomUUID();
        String jsonFileName = "top250_" + LocalDateTime.now() + ".json";

        JsonArray jsonArray = new JsonArray();

        int count = 0;
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (String imdbId : top250IdsAndRatings.keySet()) {
            if (count == 20) {
                break;
            }
            JsonObject jsonObject = myApiFilmsService.requestMovieDataAsJsonForImdbId(imdbId);
            jsonObject.addProperty("rating", top250IdsAndRatings.get(imdbId));
            jsonObject.addProperty("imdbTop250Rank", atomicInteger.getAndIncrement());
            jsonArray.add(jsonObject);
            count++;
        }

        try (FileWriter fileWriter = new FileWriter("src/main/resources/data/" + jsonFileName)) {
            fileWriter.write(jsonArray.toString());
        }
    }

    public void initTitles() {
        if (titleRepository.count() == 0) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/top250_24f28252-acfa-46e5-aa9d-e8e1e8e4f3d8.json"));
                ApiMovieAddDTO[] dtos = gson.fromJson(reader, ApiMovieAddDTO[].class);
                if (isTestDB()) {
                    for (ApiMovieAddDTO dto : dtos) {
                        this.createTitleFromApiDataDTO(dto);
                    }
                } else {
                    this.createTitleFromApiDataDTO(Arrays.stream(dtos).findFirst().orElseThrow(() -> new ObjectNotFoundException("Json file is empty")));
                }
                reader.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public String createNewTitlesFromIdsAndRatingsMap(LinkedHashMap<String, String> idsAndRatingsMap) {
        List<String> idsList = new ArrayList<>(idsAndRatingsMap.keySet());
        List<String> filteredList = filterExistingImdbIdTitles(idsList);
        List<String> failedToSaveIds = new ArrayList<>();

        filteredList.forEach(titleImdbId -> {
            try {
                ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataAsDTOForImdbId(titleImdbId);
                apiMovieAddDTO.setRating(idsAndRatingsMap.get(titleImdbId));
                this.createTitleFromApiDataDTO(apiMovieAddDTO);
            } catch (Exception e) {
                LOGGER.error("Couldn't save title with Imdb Id " + titleImdbId + " " + e);
                failedToSaveIds.add(titleImdbId);
            }
        });

        return getNewTitlesInfo(filteredList, failedToSaveIds);
    }

    public String createNewTitlesFromIdsList(List<String> idsList) {
        List<String> filteredList = filterExistingImdbIdTitles(idsList);
        List<String> failedToSaveIds = new ArrayList<>();

        filteredList.forEach(titleImdbId -> {
            try {
                ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataAsDTOForImdbId(titleImdbId);
                this.createTitleFromApiDataDTO(apiMovieAddDTO);
            } catch (Exception e) {
                LOGGER.error("Couldn't save title with Imdb Id " + titleImdbId + " " + e);
                failedToSaveIds.add(titleImdbId);
            }
        });

        return getNewTitlesInfo(filteredList, failedToSaveIds);
    }

    private String getNewTitlesInfo(List<String> filteredList, List<String> failedToSaveIds) {

        if (filteredList.isEmpty() && failedToSaveIds.isEmpty()) {
            return "Already in DB";
        }
        if (filteredList.size() == failedToSaveIds.size()) {
            return "Failed to save any Ids. Check the back-end logs for more info";
        }

        filteredList.removeAll(failedToSaveIds);

        String created = "Created: " + String.join(", ", filteredList);
        String failedToSave = "Failed to save: " + String.join(", ", failedToSaveIds);

        return created + "<br><br>" + failedToSave;
    }

    private void createTitleFromApiDataDTO(ApiMovieAddDTO apiMovieAddDTO) {
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
                    .imdbTop250Rank(apiMovieAddDTO.getImdbTop250Rank() == null ? null : Integer.parseInt(apiMovieAddDTO.getImdbTop250Rank()))
                    .metascore(Integer.valueOf(apiMovieAddDTO.getMetascore()))
                    .imdbVotes(Integer.valueOf(apiMovieAddDTO.getVotes()))
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
        } catch (Exception e) {
            LOGGER.error("Error creating title from ApiMovieAddDTO", e);
            throw new TitleCreationException(apiMovieAddDTO != null ? apiMovieAddDTO.getIdIMDB() : null, e);
        }
    }

//    public List<TitleEntity> findTop250ImdbRatedTitles() {
//        return titleRepository.findTop250ImdbRatedTitles();
//    }

    public List<TitleEntity> findTop250ImdbList() {
        return titleRepository.findTop250ImdbList();
    }

    public List<TitleEntity> find100MostPopularImdbList(){
        return titleRepository.find100MostPopularImdbList();
    }

    public List<TitleEntity> find100MostPopularOnThisSite(){
        return titleRepository.find100MostPopularOnThisSite();
    }

    public List<TitleEntity> findMostPopularImdbRatedMovies() {
        return titleRepository.findAllByType_NameAndMainTrailerURLNotNullAndPopularityNotNullOrderByPopularityAsc(TypeEnum.MOVIE);
    }



    public TitleEntity findTitleEntityByImdbId(String imdbId) {
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

    private LocalDate parseReleaseDate(String releaseDate) {

        if (releaseDate == null || releaseDate.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            LOGGER.error("Date format error");
            throw new IllegalArgumentException("Date format error", e);
        }
    }

    private void createCharacterRoles(ApiMovieAddDTO apiMovieAddDTO) {
        TitleEntity titleEntityByImdbId = this.findTitleEntityByImdbId(apiMovieAddDTO.getIdIMDB());

        apiMovieAddDTO.getActors().forEach(apiActorAddDTO -> {
            characterRoleService.createCharacterRole(
                    titleEntityByImdbId,
                    personService.findPersonEntityByImdbId(apiActorAddDTO.getId()),
                    apiActorAddDTO.getCharacter());
        });
    }

    private List<String> filterExistingImdbIdTitles(List<String> list) {
        List<String> newList = new ArrayList<>(List.copyOf(list));
        List<String> existingImdbIds = titleRepository.findAllImdbIdsByImdbIdIsIn(list);

        newList.removeAll(existingImdbIds);

        return newList;
    }

    public void updateSingleTitle(Long id) {
        TitleEntity titleEntityById = this.findTitleById(id);
        ApiMovieAddDTO apiMovieAddDTO = myApiFilmsService.requestMovieDataAsDTOForImdbId(titleEntityById.getImdbId());

        this.updateTitle(titleEntityById, apiMovieAddDTO);
    }

    private void updateTitle(TitleEntity title, ApiMovieAddDTO apiMovieAddDTO) {

        JsonObject data = updateDTOHas0RatingOrNullTrailer(title, apiMovieAddDTO);

        try {
            if (title.getMainTrailerURL() == null) {
                VideoEntity trailer = null;
                if (apiMovieAddDTO.getTrailer() == null){
                    try {
                        assert data != null;
                        if (data.has("trailerImdbId")){
                            String trailerImdbId = data.get("trailerImdbId").getAsString();
                            trailer = videoService.createNewTrailerFromIdOnly(String.valueOf(trailerImdbId));
                        }
                    }
                    catch (Exception ignored){
                    }
                }
                else {
                    trailer = videoService.createNewTrailer(apiMovieAddDTO.getTrailer());
                }
                title.setMainTrailerURL(trailer);
            }

            if (apiMovieAddDTO.getBusiness() != null) {
                title.setBoxOfficeGrossUsa(apiMovieAddDTO.getBusiness().getGrossUsa());
                title.setBoxOfficeWorldwide(apiMovieAddDTO.getBusiness().getWorldwide());
                title.setBoxOfficeOpeningWeekend(apiMovieAddDTO.getBusiness().getOpeningWeekend());
            }


            List<GenreEntity> genreEntities = this.getGenreEntities(apiMovieAddDTO);
            title.setGenres(genreEntities);




            if (title.getMainPosterURL() == null) {
                title.setMainPosterURL(photoService.createNewMainPosterFromApiDataDTO(apiMovieAddDTO));
            }

            LinkedHashMap<String, PersonEntity> peoplesMap = personService.findPersonEntitiesFromApiPersonDTOs(apiMovieAddDTO.getActors(), apiMovieAddDTO.getDirectors(), apiMovieAddDTO.getWriters());

            List<PersonEntity> actors = this.getPersonEntities(apiMovieAddDTO.getActors(), peoplesMap);
            List<PersonEntity> directors = this.getPersonEntities(apiMovieAddDTO.getDirectors(), peoplesMap);
            List<PersonEntity> writers = this.getPersonEntities(apiMovieAddDTO.getWriters(), peoplesMap);

            title.setActors(actors);
            title.setDirectors(directors);
            title.setWriters(writers);

            title.setRuntime(Long.valueOf(apiMovieAddDTO.getRuntime()));


            float rating = Float.parseFloat(apiMovieAddDTO.getRating());
            if (rating != 0) {
                title.setImdbRating(Float.parseFloat(apiMovieAddDTO.getRating()));
            }
            else {
                assert data != null;
                if (data.has("imdbRating")){
                    try {
                        title.setImdbRating(Float.parseFloat(data.get("imdbRating").getAsString()));
                    }
                    catch (Exception ignored){

                    }
                }
            }

            characterRoleService.deleteAllCharacterRolesForTitle(title);
            this.createCharacterRoles(apiMovieAddDTO);

            titleRepository.saveAndFlush(title);
        } catch (Exception e) {
            LOGGER.error("Couldn't update this title " + title.getImdbId() + " because of this error " + e);
        }
    }

    private JsonObject updateDTOHas0RatingOrNullTrailer(TitleEntity title, ApiMovieAddDTO apiMovieAddDTO) {
        JsonObject data = null;
        if (apiMovieAddDTO.getRating().equals("0") || apiMovieAddDTO.getTrailer() == null){
            try {
                data = imdbScrapeService.getTitleData(title.getImdbId());
            }
            catch (Exception e){
                return null;
            }
        }
        return data;
    }

    public List<TitleSearchViewDTO> getSearchSuggestionsContaining(String search) {
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

    public TitleEntity findTitleById(Long id) {
        return titleRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Title with id " + id + " was not found"));
    }

    public List<TitleCarouselViewDTO> get18TopRatedCarouselViewDTOs() {
        List<TitleEntity> top250ImdbRatedTitles = this.findTop250ImdbList().stream().limit(18).toList();

        return mapTitleCarouselViewDTOS(top250ImdbRatedTitles);
    }

    public List<TitleCarouselViewDTO> get4MostPopularCarouselViewDTOs() {
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

    public void updateImdb100MostPopular() {
        LinkedHashMap<String, String> mostPopularIdsAndPopularity = imdbScrapeService.get100MostPopularIdsAndPopularity();

        ArrayList<String> idsList = new ArrayList<>(mostPopularIdsAndPopularity.keySet());
        List<String> newTitleIds = this.filterExistingImdbIdTitles(idsList);

        createNewTitlesFromIdsList(newTitleIds);

        List<TitleEntity> allByImdbIdIsIn = titleRepository.findAllByImdbIdIsIn(idsList);

        clearImdbPopularity();
        allByImdbIdIsIn.forEach(title -> title.setPopularity(Integer.valueOf(mostPopularIdsAndPopularity.get(title.getImdbId()))));
        titleRepository.saveAllAndFlush(allByImdbIdIsIn);

    }

    public void updateImdbTop250Ranks() {
        LinkedHashMap<String, String> top250IdsAndRatings = imdbScrapeService.getTop250IdsAndRatings();

        ArrayList<String> idsList = new ArrayList<>(top250IdsAndRatings.keySet());
        List<String> newTitleIds = this.filterExistingImdbIdTitles(idsList);

        createNewTitlesFromIdsList(newTitleIds);

        List<TitleEntity> allByImdbIdIsIn = titleRepository.findAllByImdbIdIsIn(idsList);
        allByImdbIdIsIn.forEach(title -> title.setImdbRating(Float.parseFloat(top250IdsAndRatings.get(title.getImdbId()))));

        AtomicInteger atomicInteger = new AtomicInteger(1);
        top250IdsAndRatings.forEach((id, value) -> {
            allByImdbIdIsIn.stream().filter(title -> title.getImdbId().equals(id)).forEach(title -> title.setImdbTop250Rank(atomicInteger.getAndIncrement()));
        });

        titleRepository.saveAllAndFlush(allByImdbIdIsIn);
    }


    private void clearImdbPopularity() {
        List<TitleEntity> allByPopularityNotNull = titleRepository.findAllByPopularityNotNull();
        allByPopularityNotNull.forEach(title -> title.setPopularity(null));
        titleRepository.saveAllAndFlush(allByPopularityNotNull);
    }

    @Transactional
    public TitleVideoViewDTO getTitleVideoViewDTOByVideoImdbId(String videoImdbId) {
        TitleEntity title = findTitleByVideoImdbId(videoImdbId);
        return modelMapper.map(title, TitleVideoViewDTO.class);
    }

    private TitleEntity findTitleByVideoImdbId(String videoImdbId) {
        return titleRepository.findTitleEntityByMainTrailerURL_VideoImdbId(videoImdbId).orElseThrow(() -> new ObjectNotFoundException("Video with id " + videoImdbId + " was not found"));
    }

    @Transactional
    public TitleViewDTO getTitleViewDTOById(Long id) {
        TitleEntity titleById = this.findTitleById(id);
        return modelMapper.map(titleById, TitleViewDTO.class);
    }

    @Transactional
    public List<TitleViewDTO> getTop250TitleViewDTOs() {
        List<TitleEntity> top250ImdbRatedMovies = this.findTop250ImdbList();
        return mapTitleViewDTOS(top250ImdbRatedMovies);
    }

    @Transactional
    public List<TitleViewDTO> get100MostPopularTitleViewDTOs() {
        List<TitleEntity> mostPopularImdbRatedMovies = this.find100MostPopularImdbList();
        return mapTitleViewDTOS(mostPopularImdbRatedMovies);
    }

    @Transactional
    public List<TitleViewDTO> get100MostPopularOnThisSiteTitleViewDTOs() {
        List<TitleEntity> mostPopularImdbRatedMovies = this.find100MostPopularOnThisSite();
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

//    public List<TitleViewDTO> getTitleViewDTOsFromIdsList(List<Long> idsList) {
//        List<TitleEntity> allById = titleRepository.findAllById(idsList);
//
//        return mapTitleViewDTOS(allById);
//    }

    public void updateTitlePageViews(Long titleId, Integer count) {
        try {
            TitleEntity titleById = this.findTitleById(titleId);
            titleById.setPageViews(titleById.getPageViews() == null ? count : titleById.getPageViews() + count);
            titleRepository.saveAndFlush(titleById);
        } catch (Exception e) {
            LOGGER.error("Couldn't increment page views for titleId {} because of this error {}", titleId, e.getMessage());
        }
    }

    private boolean isTestDB() throws SQLException {
        try {
            String dbName = dataSource.getConnection().getCatalog();
            return dbName.contains("test");
        } catch (Exception e) {
            throw new RuntimeException(e);
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

