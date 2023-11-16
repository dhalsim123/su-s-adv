package com.example.imdbg.model.entity.users.dto.binding;

import com.example.imdbg.validations.availableEmailCheck.AvailableEmailCheck;
import com.example.imdbg.validations.availableUsernameCheck.AvailableUsernameCheck;
import com.example.imdbg.validations.confirmPasswordMatcher.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@PasswordMatch
public class UserRegisterDTO {

    @Size (min = 3, max = 20, message = "Username must between 3 and 20 characters")
    @AvailableUsernameCheck
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Username can only contain alphanumeric characters, underscores, and hyphens")
    private String username;

    @Email
    @NotBlank (message = "Must be a valid email")
    @AvailableEmailCheck
    private String email;

    @Size (min = 5, max = 50, message = "Password length must between 5 and 50 characters")
    private String password;

    private String confirmPassword;
}
