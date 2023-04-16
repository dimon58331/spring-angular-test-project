package com.example.springangular.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {

    private String username;
    @NotEmpty
    private String message;
}
