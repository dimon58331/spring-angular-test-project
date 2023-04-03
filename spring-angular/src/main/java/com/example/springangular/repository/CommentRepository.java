package com.example.springangular.repository;

import com.example.springangular.entity.Comment;
import com.example.springangular.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByIdAndPersonId(Long commentId, Long personId);
}
