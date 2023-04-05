package com.example.springangular.controller;

import com.example.springangular.dto.PostDTO;
import com.example.springangular.entity.Post;
import com.example.springangular.service.PostService;
import com.example.springangular.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
public class PostController {
    private final ModelMapper modelMapper;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(ModelMapper modelMapper, PostService postService, ResponseErrorValidation responseErrorValidation) {
        this.modelMapper = modelMapper;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult
            , Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;
        Post createdPost = postService.createPost(convertPostDTOToPost(postDTO), principal);
        PostDTO createdPostDTO = convertPostToPostDTO(createdPost);

        return new ResponseEntity<>(createdPostDTO, HttpStatus.OK);
    }
    
    public Post convertPostDTOToPost(PostDTO postDTO){
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO convertPostToPostDTO(Post post){
        return modelMapper.map(post, PostDTO.class);
    }
}
