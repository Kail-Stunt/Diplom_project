package ru.diplom.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.dto.request.CreateOrUpdateCommentRequest;
import ru.diplom.dto.response.CommentResponse;
import ru.diplom.dto.response.CommentsResponse;
import ru.diplom.entity.User;
import ru.diplom.service.CommentService;
import ru.diplom.service.UserService;

@RestController
@RequestMapping("/ads/{adId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CommentsResponse> getComments(@PathVariable Integer adId) {
        CommentsResponse response = commentService.getComments(adId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Integer adId,
            @Valid @RequestBody CreateOrUpdateCommentRequest request) {
        User currentUser = userService.getCurrentUser();
        CommentResponse response = commentService.addComment(adId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId) {
        User currentUser = userService.getCurrentUser();
        commentService.deleteComment(adId, commentId, currentUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @Valid @RequestBody CreateOrUpdateCommentRequest request) {
        User currentUser = userService.getCurrentUser();
        CommentResponse response = commentService.updateComment(adId, commentId, request, currentUser);
        return ResponseEntity.ok(response);
    }
}
