package com.example.angular_spring.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String username = "Invalid username";
    private String password = "Invalid password";
}
