package com.example.angular_spring.repository;

import com.example.angular_spring.entity.Comment;
import com.example.angular_spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Optional<Comment> findByIdAndPersonId(Long commentId, Long personId);
}
