package com.example.springangular.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostDTO {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String location;
}
