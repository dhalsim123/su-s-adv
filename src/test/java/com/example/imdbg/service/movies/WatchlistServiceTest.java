package com.example.imdbg.service.movies;

import com.example.imdbg.model.entity.movies.TitleEntity;
import com.example.imdbg.model.entity.users.UserEntity;
import com.example.imdbg.service.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TitleService titleService;

    private WatchlistService toTest;

    private UserEntity testUser;
    private Principal testPrincipal;
    private TitleEntity testTitle;
    private List<TitleEntity> testWatchlist;

    @BeforeEach
    void setUp() {
        toTest = new WatchlistService(userService, titleService);

        String testUsername = "testUser";
        testUser = new UserEntity();
        testPrincipal = new UsernamePasswordAuthenticationToken(new User(testUsername, "", List.of(new SimpleGrantedAuthority("ROLE_USER"))), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        testTitle = new TitleEntity();
        testTitle.setId(1L);
        testWatchlist = new ArrayList<>();
        testUser.setWatchlist(testWatchlist);

    }

    @Test
    void addToWatchlist() {
        Mockito.when(titleService.findTitleById(1L)).thenReturn(testTitle);
        Mockito.when(userService.findUserByUsernameForUpdate("testUser")).thenReturn(testUser);

        toTest.addToWatchlist(1L, testPrincipal);

        Mockito.verify(userService, Mockito.times(1)).findUserByUsernameForUpdate("testUser");
        Mockito.verify(titleService, Mockito.times(1)).findTitleById(1L);
        Mockito.verify(userService, Mockito.times(1)).saveUser(testUser);

        assertEquals(testUser.getWatchlist().get(0), testTitle);
    }

    @Test
    void addToWatchlist_DoesntSaveUser_WhenTitleIsAlreadyInTheWatchlist() {
        Mockito.when(userService.findUserByUsernameForUpdate("testUser")).thenReturn(testUser);

        testUser.getWatchlist().add(testTitle);

        assertThrows(RuntimeException.class, () -> toTest.addToWatchlist(1L, testPrincipal));
    }

    @Test
    void removeFromWatchlist() {
        Mockito.when(userService.findUserByUsernameForUpdate("testUser")).thenReturn(testUser);

        testUser.getWatchlist().add(testTitle);

        toTest.removeFromWatchlist(1L, testPrincipal);

        Mockito.verify(userService, Mockito.times(1)).findUserByUsernameForUpdate("testUser");
        Mockito.verify(userService, Mockito.times(1)).saveUser(testUser);
    }
    @Test
    void removeFromWatchlist_DoesntSaveUser_WhenTitleIsntInTheWatchlist() {
        Mockito.when(userService.findUserByUsernameForUpdate("testUser")).thenReturn(testUser);

        assertThrows(RuntimeException.class, () -> toTest.removeFromWatchlist(1L, testPrincipal));

    }

    @Test
    void getLoggedUserWatchlist() {

        Mockito.when(userService.getPrincipalWatchlist(testPrincipal)).thenReturn(testUser.getWatchlist());

        testUser.getWatchlist().add(testTitle);

        toTest.getLoggedUserWatchlist(testPrincipal);

        Mockito.verify(userService, Mockito.times(1)).getPrincipalWatchlist(testPrincipal);
        Mockito.verify(titleService, Mockito.times(1)).mapTitleViewDTOS(userService.getPrincipalWatchlist(testPrincipal));

    }
}