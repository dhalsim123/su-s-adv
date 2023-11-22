package com.example.imdbg.init;

import com.example.imdbg.service.movies.GenreService;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.movies.TypeService;
import com.example.imdbg.service.users.RoleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class FirstInit implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final GenreService genreService;
    private final TypeService typeService;
    private final TitleService titleService;

    private final DataSource dataSource;

    public FirstInit(UserService userService, RoleService roleService, GenreService genreService, TypeService typeService, TitleService titleService, DataSource dataSource) {
        this.userService = userService;
        this.roleService = roleService;
        this.genreService = genreService;
        this.typeService = typeService;
        this.titleService = titleService;
        this.dataSource = dataSource;
    }


    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
        userService.initAdmin();

        genreService.initGenres();
        typeService.initTypes();

        if (isTestDB()) {
            titleService.initTitlesFromJson_TestDB();
        }
        else {
            titleService.initTitlesFromJson();
        }
    }

    private boolean isTestDB() throws SQLException {
        try {
            String dbName = dataSource.getConnection().getCatalog();
            return dbName.contains("test");
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
