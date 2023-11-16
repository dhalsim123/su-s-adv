package com.example.imdbg.model.entity.users.dto.binding;

import com.example.imdbg.validations.availableUsernameCheck.AvailableUsernameCheck;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeUsernameDTO {

    @Size(min = 3, max = 20, message = "Username must between 3 and 20 characters")
    @AvailableUsernameCheck
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Username can only contain alphanumeric characters, underscores, and hyphens")
    private String username;
}
