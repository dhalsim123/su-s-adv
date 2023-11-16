package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.PersonEntity;
import com.example.imdbg.model.entity.movies.apidtos.ApiPersonAddDTO;
import com.example.imdbg.model.entity.movies.dtos.view.person.PersonPageViewDTO;
import com.example.imdbg.model.exceptions.ObjectNotFoundException;
import com.example.imdbg.repository.movies.PersonRepository;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PersonService {
    private PersonRepository personRepository;

    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }



    public LinkedHashMap<String, PersonEntity> findPersonEntitiesFromApiPersonDTOs(List<ApiPersonAddDTO> actors, List<ApiPersonAddDTO> directors, List<ApiPersonAddDTO> writers){
        LinkedHashMap<String, ApiPersonAddDTO> uniqueApiPersonDataDTOS = this.filterUniqueApiPersonDataDTOS(actors, directors, writers);

        LinkedHashMap<String, PersonEntity> peoplesMap = new LinkedHashMap<>();

        uniqueApiPersonDataDTOS.forEach((id, dto) -> peoplesMap.putIfAbsent(id, this.getPersonEntityFromApiDataDTO(dto)));

        return peoplesMap;
    }

    private LinkedHashMap<String, ApiPersonAddDTO> filterUniqueApiPersonDataDTOS (List<ApiPersonAddDTO> actors, List<ApiPersonAddDTO> directors, List<ApiPersonAddDTO> writers){

        LinkedHashMap<String, ApiPersonAddDTO> uniquePeopleDTOsMap = new LinkedHashMap<>();

        actors.forEach(dto -> uniquePeopleDTOsMap.putIfAbsent(dto.getId(), dto));
        directors.forEach(dto -> uniquePeopleDTOsMap.putIfAbsent(dto.getId(), dto));
        writers.forEach(dto -> uniquePeopleDTOsMap.putIfAbsent(dto.getId(), dto));

        return uniquePeopleDTOsMap;
    }


    private PersonEntity getPersonEntityFromApiDataDTO(ApiPersonAddDTO dto){
        return personRepository.findPersonEntityByIdIMDB(dto.getId()).orElseGet(() -> createPersonEntityFromApiDataDTO(dto));
    }

    private PersonEntity createPersonEntityFromApiDataDTO(ApiPersonAddDTO dto){
        return personRepository.saveAndFlush(PersonEntity.builder()
                .name(dto.getName())
                .idIMDB(dto.getId())
                .photoUrl(dto.getUrlPhoto())
                .build());
    }

    public PersonEntity findPersonEntityByImdbId(String imdbId){
        return personRepository.findPersonEntityByIdIMDB(imdbId).orElseThrow(() -> new ObjectNotFoundException("Person with IMDB Id " + imdbId + " was not found"));
    }

    public PersonEntity findPersonEntityById(Long id){
        return personRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Person with Id " + id + " was not found"));
    }

    public PersonEntity findPersonEntityByIdWithTitles(Long id){
        return personRepository.findPersonEntityByIdWithTitles(id).orElseThrow(() -> new ObjectNotFoundException("Person with Id " + id + " was not found"));
    }

    @Transactional
    public PersonPageViewDTO getPersonPageViewDTO(Long id){
        PersonEntity personEntity = this.findPersonEntityById(id);

        PersonPageViewDTO personPageViewDTO = modelMapper.map(personEntity, PersonPageViewDTO.class);

        personPageViewDTO.setBestRatedTitleVideoImdbAndKnownFor();
        personPageViewDTO.setTitles();

        return personPageViewDTO;
    }

//    private PersonEntity findOrCreatePersonEntityFromApiDataDTO(ApiPersonAddDTO dto){
//
//        Optional<PersonEntity> personEntityByIdIMDB = personRepository.findPersonEntityByIdIMDB(dto.getId());
//
//        PersonEntity personEntity = null;
//
//        if (personEntityByIdIMDB.isEmpty()) {
//            personEntity = personRepository.saveAndFlush(PersonEntity.builder()
//                    .name(dto.getName())
//                    .idIMDB(dto.getId())
//                    .photoUrl(dto.getUrlPhoto())
//                    .build());
//        }
//
//        return personEntity;
//
//
//    }
}
