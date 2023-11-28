package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleCarouselViewDTO;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final TitleService titleService;
    private final UserService userService;

    public HomeController(TitleService titleService, UserService userService) {
        this.titleService = titleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView getHome(ModelAndView modelAndView, Principal principal){

        List<TitleCarouselViewDTO> topRatedCarouselViewDTOs = titleService.get18TopRatedCarouselViewDTOs();
        modelAndView.addObject("topRatedCarouselViewDTOs", topRatedCarouselViewDTOs);

        List<TitleCarouselViewDTO> top24OnImdbThisWeekCarouselViewDTOs = titleService.getTop24OnImdbThisWeekCarouselViewDTOs();
        modelAndView.addObject("top24OnImdbThisWeekCarouselViewDTOs", top24OnImdbThisWeekCarouselViewDTOs);

        List<TitleCarouselViewDTO> mostPopularOnThisSiteCarouselViewDTOs = titleService.get6MostPopularOnThisSiteCarouselViewDTOs();
        modelAndView.addObject("mostPopularOnThisSiteCarouselViewDTOs", mostPopularOnThisSiteCarouselViewDTOs);

        List<TitleCarouselViewDTO> fourMostPopular = titleService.get4MostPopularCarouselViewDTOs();
        modelAndView.addObject("fourMostPopular", fourMostPopular);

        if (principal != null){
            List<Long> principalWatchlistIds = userService.getPrincipalWatchlistIds(principal);
            modelAndView.addObject("principalWatchlistIds", principalWatchlistIds);
        }

        modelAndView.setViewName("index");

        return modelAndView;
    }
}
