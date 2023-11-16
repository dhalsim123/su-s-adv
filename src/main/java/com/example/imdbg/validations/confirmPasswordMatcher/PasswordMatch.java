package com.example.imdbg.validations.confirmPasswordMatcher;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@Constraint(validatedBy = PasswordMatcher.class)
public @interface PasswordMatch {
    String message() default "Passwords dont match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
