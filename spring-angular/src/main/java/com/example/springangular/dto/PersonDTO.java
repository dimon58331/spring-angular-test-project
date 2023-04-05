package com.example.springangular.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PersonDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    private String surname;
    @NotEmpty
    private String email;
    @NotEmpty
    private String bio;
    @NotEmpty
    private String username;
}
