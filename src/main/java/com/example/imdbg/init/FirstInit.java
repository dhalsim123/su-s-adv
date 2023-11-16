package com.example.imdbg.init;

import com.example.imdbg.service.movies.GenreService;
import com.example.imdbg.service.movies.TypeService;
import com.example.imdbg.service.users.RoleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FirstInit implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final GenreService genreService;
    private final TypeService typeService;

    public FirstInit(UserService userService, RoleService roleService, GenreService genreService, TypeService typeService) {
        this.userService = userService;
        this.roleService = roleService;
        this.genreService = genreService;
        this.typeService = typeService;
    }


    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
        userService.initAdmin();

        genreService.initGenres();
        typeService.initTypes();
    }
}
