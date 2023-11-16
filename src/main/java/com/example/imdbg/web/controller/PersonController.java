package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.person.PersonPageViewDTO;
import com.example.imdbg.service.movies.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable Long id, ModelAndView modelAndView){

        PersonPageViewDTO personPageViewDTO = personService.getPersonPageViewDTO(id);

        modelAndView.addObject("personPageViewDTO", personPageViewDTO);
        modelAndView.setViewName("personPage");

        return modelAndView;
    }
}
