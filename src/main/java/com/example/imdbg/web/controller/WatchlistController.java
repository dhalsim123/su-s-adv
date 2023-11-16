package com.example.imdbg.web.controller;

import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.movies.WatchlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final Logger LOGGER = LoggerFactory.getLogger(TitleService.class);

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public ModelAndView getUserWatchlist(Principal principal, ModelAndView modelAndView){
        List<TitleViewDTO> watchlist = watchlistService.getLoggedUserWatchlist(principal);

        modelAndView.addObject("watchlist", watchlist);

        modelAndView.setViewName("userWatchlist");

        return modelAndView;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity postAdd(@PathVariable Long id, Principal principal){
        try {
            watchlistService.addToWatchlist(id, principal);
            LOGGER.info("added to watchlist {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("{} wasn't added to watchlist because of this error: " + e, id);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity postRemove(@PathVariable Long id, Principal principal){
        try {
            watchlistService.removeFromWatchlist(id, principal);
            LOGGER.info("removed from watchlist {}", id);
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            LOGGER.error("{} wasn't removed from the watchlist because of this error: " + e, id);
            return ResponseEntity.badRequest().build();
        }
    }


}
