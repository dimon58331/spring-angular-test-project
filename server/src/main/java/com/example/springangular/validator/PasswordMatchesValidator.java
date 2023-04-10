package com.example.springangular.validator;

import com.example.springangular.annotation.PasswordMatches;
import com.example.springangular.payload.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        RegistrationRequest registrationRequest = (RegistrationRequest) value;
        return registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword());
    }
}
