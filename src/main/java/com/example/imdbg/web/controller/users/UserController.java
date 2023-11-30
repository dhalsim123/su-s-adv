package com.example.imdbg.web.controller.users;

import com.example.imdbg.model.entity.users.dto.binding.ChangeUsernameDTO;
import com.example.imdbg.model.entity.users.dto.view.UserSettingsDTO;
import com.example.imdbg.service.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public ModelAndView getSettings(Principal principal, ModelAndView modelAndView){

        UserSettingsDTO userSettingsDTO = userService.getPrincipalUserSettingsDTO(principal);

        modelAndView.addObject("userSettingsDTO", userSettingsDTO);

        modelAndView.setViewName("userSettings");

        return modelAndView;
    }

    @GetMapping("/settings/change-username")
    public ModelAndView getChangeUsername(Principal principal, ModelAndView modelAndView){
        modelAndView.setViewName("change-username");

        return modelAndView;
    }

    @PatchMapping("/settings/change-username")
    public ModelAndView patchChangeUsername(@Validated ChangeUsernameDTO changeUsernameDTO,
                                            BindingResult bindingResult,
                                            RedirectAttributes redirectAttributes,
                                            Principal principal,
                                            HttpServletRequest request){

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("changeUsernameDTO", changeUsernameDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.changeUsernameDTO", bindingResult);

            return new ModelAndView("redirect:/users/settings/change-username");
        }

        userService.changeUsername(changeUsernameDTO, principal);

        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }

        return new ModelAndView("redirect:/users/settings");
    }

    @ModelAttribute
    public ChangeUsernameDTO initChangeUsernameDTO(){
        return new ChangeUsernameDTO();
    }
}
