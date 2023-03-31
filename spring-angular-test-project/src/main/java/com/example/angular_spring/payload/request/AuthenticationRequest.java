package com.example.angular_spring.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class AuthenticationRequest {
    @NotEmpty(message = "Username should be filling")
    @Size(min = 4, message = "Size should be more than 4 characters")
    private String username;
    @NotEmpty(message = "Password should be filling")
    @Size(min = 6, message = "Size should be more than 6 characters")
    private String password;
}
