package com.example.imdbg.web.controller.admin;

import com.example.imdbg.model.entity.users.dto.view.UserSettingsDTO;
import com.example.imdbg.service.admin.AdminService;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/panel")
    public ModelAndView getAdminPanel(ModelAndView modelAndView){

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
    public ModelAndView getUserById(@PathVariable Long id, ModelAndView modelAndView){

        UserSettingsDTO userSettingsDTOById = userService.getUserSettingsDTOById(id);

        modelAndView.addObject("user", userSettingsDTOById);

        modelAndView.setViewName("adminPanel-user-by-id");

        return modelAndView;
    }

    @PatchMapping("/users/{id}/remove-admin-role")
    public ModelAndView removeAdminRole(@PathVariable Long id, ModelAndView modelAndView, Principal principal){

        adminService.removeAdminRole(id, principal);

        modelAndView.setViewName("redirect:/admin/users/{id}");

        return modelAndView;
    }

    @PatchMapping("/users/{id}/add-admin-role")
    public ModelAndView addAdminRole(@PathVariable Long id, ModelAndView modelAndView, Principal principal){

        adminService.addAdminRole(id, principal);

        modelAndView.setViewName("redirect:/admin/users/{id}");

        return modelAndView;
    }


}
