package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleVideoViewDTO;
import com.example.imdbg.service.movies.TitleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/video")
public class VideoController {

    private final TitleService titleService;

    public VideoController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable String id, ModelAndView modelAndView) throws IOException {

        TitleVideoViewDTO titleVideoViewDTO = titleService.getTitleVideoViewDTOByVideoImdbId(id);

        modelAndView.addObject("titleVideoViewDTO", titleVideoViewDTO);
        modelAndView.setViewName("videoPage");

        return modelAndView;
    }


    @ModelAttribute
    public TitleVideoViewDTO initTitleViewDTO(){
        return new TitleVideoViewDTO();
    }
}
