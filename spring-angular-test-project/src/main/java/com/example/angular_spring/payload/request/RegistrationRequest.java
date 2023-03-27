package com.example.angular_spring.payload.request;

import com.example.angular_spring.annotation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class RegistrationRequest {
    @NotEmpty(message = "Field username is required")
    private String username;
    @NotEmpty(message = "Field email is required")
    @Email
    private String email;
    @NotEmpty(message = "Field name is required")
    private String name;
    @NotEmpty(message = "Field surname is required")
    private String surname;
    @NotEmpty(message = "Field password is required")
    @Size(min = 6, message = "Size should be more than 6 characters")
    private String password;
    @NotEmpty(message = "Field confirm password is required")
    @Size(min = 6, message = "Size should be more than 6 characters")
    private String confirmPassword;
}
