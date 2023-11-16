package com.example.imdbg.model.entity.users.dto.binding;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class    UserLoginDTO {

    @Size(min = 3, max = 20, message = "Length must between 5 and 20 characters")
    private String username;

    @Size(min = 5, max = 20, message = "Length must between 5 and 20 characters")
    private String password;
}
