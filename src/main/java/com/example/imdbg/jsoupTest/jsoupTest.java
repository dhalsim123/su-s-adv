package com.example.imdbg.jsoupTest;

import com.example.imdbg.model.entity.movies.apidtos.ApiMovieAddDTO;
import com.example.imdbg.repository.movies.*;
import com.example.imdbg.repository.users.UserRepository;
import com.example.imdbg.service.api.MyApiFilmsService;
import com.example.imdbg.service.movies.GenreService;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.movies.TypeService;
import com.example.imdbg.service.scrape.ImdbScrapeService;
import com.google.gson.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class jsoupTest implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final TitleRepository titleRepository;

    private final GenreRepository genreRepository;

    private final GenreService genreService;
    private final TypeRepository typeRepository;

    private final TypeService typeService;

    private final TitleService titleService;

    private final ImdbScrapeService imdbScrapeService;

    private final MyApiFilmsService myApiFilmsService;

    private final CharacterRoleRepository characterRoleRepository;
    private final Gson gson;

    public jsoupTest(UserRepository userRepository, PersonRepository personRepository, TitleRepository titleRepository, GenreRepository genreRepository, GenreService genreService, TypeRepository typeRepository, TypeService typeService, TitleService titleService, ImdbScrapeService imdbScrapeService, MyApiFilmsService myApiFilmsService, CharacterRoleRepository characterRoleRepository, Gson gson) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.titleRepository = titleRepository;
        this.genreRepository = genreRepository;
        this.genreService = genreService;
        this.typeRepository = typeRepository;
        this.typeService = typeService;
        this.titleService = titleService;
        this.imdbScrapeService = imdbScrapeService;
        this.myApiFilmsService = myApiFilmsService;
        this.characterRoleRepository = characterRoleRepository;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {

//        titleService.fetchSingleImdbTitle("tt7428530");
//        titleService.updateImdbTop250();
//        titleService.updateImdb100MostPopular();
//        titleService.fetchTop250ImdbTitles();
//        titleService.tryToUpdateTitlesReleasedPastMonth();
//        titleService.fetch250Titles(10);
    }
}


//List<ApiPersonAddDTO> actorDTOs = apiMovieAddDTO.getActors();
//            List<ApiPersonAddDTO> directorDTOs = apiMovieAddDTO.getDirectors();
//            List<ApiPersonAddDTO> writerDTOs = apiMovieAddDTO.getWriters();
//
//
//            List<String> actorIds = apiMovieAddDTO.getActors().stream().map(ApiPersonAddDTO::getId).toList();
//            List<String> directorIds = apiMovieAddDTO.getDirectors().stream().map(ApiPersonAddDTO::getId).toList();
//            List<String> writerIds = apiMovieAddDTO.getWriters().stream().map(ApiPersonAddDTO::getId).toList();
//
//
//            LinkedHashMap<String, ApiPersonAddDTO> apiPersonAddDTOS = new LinkedHashMap<>();
//
//            actorDTOs.forEach(personAddDTO -> apiPersonAddDTOS.putIfAbsent(personAddDTO.getId(), personAddDTO));
//            directorDTOs.forEach(personAddDTO -> apiPersonAddDTOS.putIfAbsent(personAddDTO.getId(), personAddDTO));
//            writerDTOs.forEach(personAddDTO -> apiPersonAddDTOS.putIfAbsent(personAddDTO.getId(), personAddDTO));
//
//            LinkedHashMap<String, PersonEntity> personMap = new LinkedHashMap<>();
//
//            apiPersonAddDTOS.forEach((id, dto) ->
//                    personMap.putIfAbsent(id, personRepository.findPersonEntityByIdIMDB(id).orElse(
//                                                    PersonEntity.builder()
//                                                            .name(dto.getName())
//                                                            .idIMDB(id)
//                                                            .photoUrl(dto.getUrlPhoto())
//                                                            .build())));
//
//            List<PersonEntity> actorsList = new ArrayList<>();
//            List<PersonEntity> directorsList = new ArrayList<>();
//            List<PersonEntity> writersList = new ArrayList<>();
//            actorIds.forEach(id -> actorsList.add(personMap.get(id)));
//            directorIds.forEach(id -> directorsList.add(personMap.get(id)));
//            writerIds.forEach(id -> writersList.add(personMap.get(id)));
//
//            titleEntity.setActors(actorsList);
//            titleEntity.setDirectors(directorsList);
//            titleEntity.setWriters(writersList);





//    String urlGenres = "https://m.imdb.com/search/title/";
//    List<String> genresList = new ArrayList<>();
//        try {
//                Document doc = Jsoup.connect(urlGenres).get();
//                Elements genres = doc.select("input[name=genres]");
//
//                genresList = genres.stream().map(Element::val).collect(Collectors.toList());
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//
//                genresList.forEach(System.out::println);
//                System.out.println(genresList.size());


//        String url = "https://www.imdb.com/chart/top/";
//        try {
//            Document doc = Jsoup.connect(url).get();
//            Elements liItems = doc.select("li.ipc-metadata-list-summary-item");
//
//            for (Element li : liItems) {
//                String href = li.select("a.ipc-lockup-overlay").attr("href");
//                String movieId = href.replaceAll("/title/(.*?)/.*", "$1");
//                System.out.println(movieId);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    RestTemplate restTemplate = new RestTemplate();
//    String urlApi = "https://www.myapifilms.com/imdb/idIMDB?idIMDB=tt28814949&token=398c275c-acc7-4d0a-a561-931c5c39d99c&format=json&language=en-us&aka=0&business=0&seasons=0&seasonYear=0&technical=0&trailers=0&movieTrivia=0&awards=0&moviePhotos=0&movieVideos=0&actors=0&biography=0&uniqueName=0&filmography=0&bornDied=0&starSign=0&actorActress=0&actorTrivia=0&similarMovies=0&goofs=0&keyword=0&quotes=0&fullSize=0&companyCredits=0&filmingLocations=0&directors=1&writers=1";
//
//    ResponseEntity<String> response = restTemplate.getForEntity(urlApi, String.class);
//        System.out.println(response.getBody());