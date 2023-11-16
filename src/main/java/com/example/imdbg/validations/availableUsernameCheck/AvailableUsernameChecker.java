package com.example.imdbg.validations.availableUsernameCheck;

import com.example.imdbg.repository.users.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AvailableUsernameChecker implements ConstraintValidator<AvailableUsernameCheck, String> {

    private final UserRepository userRepository;

    public AvailableUsernameChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(AvailableUsernameCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findUserEntityByUsername(username).isEmpty();
    }

}
