package com.example.springangular.controller;

import com.example.springangular.dto.CommentDTO;
import com.example.springangular.entity.Comment;
import com.example.springangular.payload.response.MessageResponse;
import com.example.springangular.service.CommentService;
import com.example.springangular.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper,
                             ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@PathVariable("postId") String postId,
                                                @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult,
                                                Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), convertCommentDTOToComment(commentDTO),
                principal);
        CommentDTO createdComment = convertCommentToCommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") String postId){
        List<CommentDTO> commentDTOList = commentService.findAllCommentsByPost(Long.parseLong(postId))
                .stream().map(this::convertCommentToCommentDTO).toList();
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId){
        commentService.deleteComment(Long.parseLong(commentId));

        return new ResponseEntity<>(new MessageResponse("Comment was deleted successfully"), HttpStatus.OK);
    }

    public Comment convertCommentDTOToComment(CommentDTO commentDTO){
        return modelMapper.map(commentDTO, Comment.class);
    }
    public CommentDTO convertCommentToCommentDTO(Comment comment){
        return modelMapper.map(comment, CommentDTO.class);
    }
}
