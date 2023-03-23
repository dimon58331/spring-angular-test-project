package com.example.angular_spring.validator;

import com.example.angular_spring.annotation.PasswordMatches;
import com.example.angular_spring.payload.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Annotation;

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
