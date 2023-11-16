package com.example.imdbg.web.controller.users.admin;

import com.example.imdbg.model.entity.users.dto.view.UserSettingsDTO;
import com.example.imdbg.service.movies.TitleService;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
    public ModelAndView getAdmin(ModelAndView modelAndView){

        modelAndView.setViewName("adminPanel");

        return modelAndView;
    }

    @GetMapping("/users/all")
    public ModelAndView getAllUsers(ModelAndView modelAndView){

        List<UserSettingsDTO> allUserSettingsDTO = userService.getAllUserSettingsDTO();

        modelAndView.addObject("allUserSettingsDTO", allUserSettingsDTO);

        modelAndView.setViewName("adminPanel-allUsers");

        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView getAllUsers(@PathVariable Long id, ModelAndView modelAndView){

        UserSettingsDTO userSettingsDTOById = userService.getUserSettingsDTOById(id);

        modelAndView.addObject("user", userSettingsDTOById);

        modelAndView.setViewName("adminPanel-user-by-id");

        return modelAndView;
    }

    @PatchMapping("/users/{id}/remove-admin-role")
    public ModelAndView removeAdminRole(@PathVariable Long id, ModelAndView modelAndView, Principal principal){

        userService.removeAdminRole(id, principal);

        modelAndView.setViewName("redirect:/admin/users/{id}");

        return modelAndView;
    }

    @PatchMapping("/users/{id}/add-admin-role")
    public ModelAndView addAdminRole(@PathVariable Long id, ModelAndView modelAndView, Principal principal){

        userService.addAdminRole(id, principal);

        modelAndView.setViewName("redirect:/admin/users/{id}");

        return modelAndView;
    }


}
