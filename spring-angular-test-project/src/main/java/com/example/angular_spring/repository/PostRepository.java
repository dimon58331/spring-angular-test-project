package com.example.angular_spring.repository;

import com.example.angular_spring.entity.Person;
import com.example.angular_spring.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPersonOrderByCreatedDateDesc(Person person);
    List<Post> findAllByOrderByCreatedDateDesc();
    List<Post> findPostByIdAndPerson(Long postId, Person person);
}
