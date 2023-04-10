package com.example.springangular.repository;

import com.example.springangular.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageModelRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByPersonId(Long personId);
    Optional<ImageModel> findByPostId(Long postId);
}
