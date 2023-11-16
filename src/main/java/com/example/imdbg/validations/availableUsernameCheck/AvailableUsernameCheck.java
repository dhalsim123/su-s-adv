package com.example.imdbg.validations.availableUsernameCheck;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = AvailableUsernameChecker.class)
public @interface AvailableUsernameCheck {
    String message() default "Username is already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
