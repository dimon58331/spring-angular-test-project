package com.example.angular_spring.repository;

import com.example.angular_spring.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByPersonId(Long personId);
    Optional<ImageModel> findByPostId(Long postId);
}
