package com.example.springangular.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

@Data
@NoArgsConstructor
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "BYTEA")
    private byte[] imageBytes;
    @JsonIgnore
    private Long personId;
    @JsonIgnore
    private Long postId;
}
