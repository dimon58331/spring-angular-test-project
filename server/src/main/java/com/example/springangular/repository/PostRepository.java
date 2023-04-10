package com.example.springangular.repository;

import com.example.springangular.entity.Person;
import com.example.springangular.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPersonOrderByCreatedDateDesc(Person person);
    List<Post> findAllByOrderByCreatedDateDesc();
    Optional<Post> findPostByIdAndPerson(Long postId, Person person);
}
