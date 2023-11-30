package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.model.entity.movies.dtos.view.TitleViewDTO;
import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class WatchlistService {

    private final UserService userService;
    private final TitleService titleService;

    public WatchlistService(UserService userService, TitleService titleService) {
        this.userService = userService;
        this.titleService = titleService;
    }

    @Transactional
    public void addToWatchlist(Long id, Principal principal){

            UserEntity userByUsername = userService.findUserByUsernameForUpdate(principal.getName());
            TitleEntity titleById = titleService.findTitleById(id);

            List<TitleEntity> watchlist = userByUsername.getWatchlist();

            if (watchlist.stream().anyMatch(title -> title.getId().equals(id))){
                throw new RuntimeException("Title is already in the watchlist");
            }

            watchlist.add(titleById);

            userService.saveUser(userByUsername);

    }

    @Transactional
    public void removeFromWatchlist(Long id, Principal principal){

            UserEntity userByUsername = userService.findUserByUsernameForUpdate(principal.getName());

            List<TitleEntity> watchlist = userByUsername.getWatchlist();

            if (!watchlist.removeIf(title -> title.getId().equals(id))){
                throw new RuntimeException("Title wasn't in the watchlist");
            }

            userService.saveUser(userByUsername);

    }

    @Transactional
    public List<TitleViewDTO> getLoggedUserWatchlist(Principal principal){
        List<TitleEntity> principalWatchlist = userService.getPrincipalWatchlist(principal);
        return titleService.mapTitleViewDTOS(principalWatchlist);
    }
}
