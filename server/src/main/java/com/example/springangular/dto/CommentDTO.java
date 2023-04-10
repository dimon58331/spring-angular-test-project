package com.example.springangular.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String message;
}
