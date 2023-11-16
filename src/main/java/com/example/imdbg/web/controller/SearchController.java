package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleSearchViewDTO;
import com.example.imdbg.service.movies.TitleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/searchSuggestions")
public class SearchController {

    private final TitleService titleService;

    public SearchController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping
    @ResponseBody
    public List<TitleSearchViewDTO> getSearchSuggestions(@RequestParam String search) {
        return titleService.getSearchSuggestionsContaining(search);
    }
}
