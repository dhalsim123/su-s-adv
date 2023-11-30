package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/title")
public class TitleController {

    private final TitleService titleService;
    private final UserService userService;

    public TitleController(TitleService titleService, UserService userService) {
        this.titleService = titleService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ModelAndView getById(@PathVariable Long id, Principal principal, ModelAndView modelAndView){


        TitleViewDTO titleViewDTO = titleService.getTitleViewDTOById(id);

        modelAndView.addObject("titleViewDTO", titleViewDTO);
        if (principal != null){
            List<Long> principalWatchlistIds = userService.getPrincipalWatchlistIds(principal);
            modelAndView.addObject("principalWatchlistIds", principalWatchlistIds);
        }

        modelAndView.addObject("currentDate", LocalDate.now());

        modelAndView.setViewName("titlePage");

        return modelAndView;
    }


}
