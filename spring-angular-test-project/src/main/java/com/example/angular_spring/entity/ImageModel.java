package com.example.angular_spring.entity;

import jakarta.persistence.*;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    private byte[] imageBytes;
    @JsonIgnore
    private Long personId;
    @JsonIgnore
    private Long postId;
}
