package com.example.imdbg.validations.availableEmailCheck;

import com.example.imdbg.repository.users.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AvailableEmailChecker implements ConstraintValidator<AvailableEmailCheck, String> {

    private final UserRepository userRepository;

    public AvailableEmailChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(AvailableEmailCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.findUserEntityByEmail(email).isEmpty();
    }

}
