package com.example.imdbg.web.controller.users;

import com.example.imdbg.model.entity.users.dto.binding.UserRegisterDTO;
import com.example.imdbg.service.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getRegister(){
        return new ModelAndView("register");
    }

    @PostMapping
    public ModelAndView postRegister (@Validated UserRegisterDTO userRegisterDTO,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("userRegisterDTO", userRegisterDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);

            return new ModelAndView("redirect:/register");
        }

        this.userService.registerUser(userRegisterDTO);

        return new ModelAndView("redirect:/");
    }

    @ModelAttribute
    public UserRegisterDTO initUserRegisterDTO(){
        return new UserRegisterDTO();
    }
}
