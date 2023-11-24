package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import com.example.imdbg.service.movies.TitleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/chart")
public class ChartController {

    private final TitleService titleService;

    public ChartController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/100MostPopular")
    public ModelAndView get100MostPopular(ModelAndView modelAndView){

        List<TitleViewDTO> mostPopularTitleViewDTOs = titleService.get100MostPopularTitleViewDTOs();

        modelAndView.addObject("titles", mostPopularTitleViewDTOs);

        modelAndView.setViewName("100MostPopular");

        return modelAndView;
    }

    @GetMapping("/top250")
    public ModelAndView getTop250(ModelAndView modelAndView){

        List<TitleViewDTO> top250TitleViewDTOs = titleService.getTop250TitleViewDTOs();

        modelAndView.addObject("titles", top250TitleViewDTOs);

        modelAndView.setViewName("top250");

        return modelAndView;
    }

    @GetMapping("/sitePopularityRankings")
    public ModelAndView getSiteSpecificPopularityRankings(ModelAndView modelAndView){

        List<TitleViewDTO> mostPopularTitleViewDTOs = titleService.get100MostPopularOnThisSiteTitleViewDTOs();

        modelAndView.addObject("titles", mostPopularTitleViewDTOs);

        modelAndView.setViewName("SitePopularityRankings");

        return modelAndView;
    }
}
