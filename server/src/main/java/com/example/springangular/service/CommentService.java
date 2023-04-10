package com.example.springangular.service;


import com.example.springangular.entity.Comment;
import com.example.springangular.entity.Person;
import com.example.springangular.entity.Post;
import com.example.springangular.exception.CommentNotFoundException;
import com.example.springangular.exception.PostNotFoundException;
import com.example.springangular.repository.CommentRepository;
import com.example.springangular.repository.PersonRepository;
import com.example.springangular.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(PersonRepository personRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment saveComment(Long postId, Comment comment, Principal principal){
        Person person = getPersonByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        comment.setPersonId(person.getId());
        comment.setUsername(person.getUsername());
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not be found"));
        commentRepository.delete(comment);
    }

    public List<Comment> findAllCommentsByPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return commentRepository.findAllByPost(post);
    }

    private Person getPersonByPrincipal(Principal principal){
        String username = principal.getName();
        return personRepository.findPersonByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }
}
