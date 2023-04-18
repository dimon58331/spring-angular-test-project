package com.example.springangular.dto;

import com.example.springangular.entity.Person;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String location;
    private Set<String> likedUsers;
    private PersonDTO person;
}
