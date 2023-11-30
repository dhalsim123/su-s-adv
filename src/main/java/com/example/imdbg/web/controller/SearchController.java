package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleSearchViewDTO;
import com.example.imdbg.model.exceptions.ForbiddenException;
import com.example.imdbg.service.movies.TitleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final TitleService titleService;

    public SearchController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/suggestions")
    @ResponseBody
    public ResponseEntity<?> getSearchSuggestions(@RequestParam(required = false) String search, HttpServletRequest request) {
//        if (search == null || search.trim().isEmpty()) {
//            throw new BadRequestException("Search parameter is required");
//        }

        if (request.getHeader("X-Requested-With") == null){
            throw new ForbiddenException("You cannot request search suggestions outside the site's search bar");
        }

        List<TitleSearchViewDTO> searchSuggestionsContaining = titleService.getSearchSuggestionsContaining(search);
        return new ResponseEntity<>(searchSuggestionsContaining, HttpStatus.OK);
    }

    @GetMapping("/results")
    public ModelAndView getSearchResults(@RequestParam(required = false) String search, ModelAndView modelAndView){

        if (search == null || search.trim().isEmpty()) {
            modelAndView.addObject("search", null);
            modelAndView.setViewName("searchResults");
            return modelAndView;
        }

        List<TitleSearchViewDTO> searchSuggestionsContaining = titleService.getSearchSuggestionsContaining(search);

        modelAndView.addObject("titles", searchSuggestionsContaining);
        modelAndView.addObject("search", search);
        modelAndView.setViewName("searchResults");

        return modelAndView;
    }

}
