package com.blog.be.comment.presentation;

import com.blog.be.comment.application.CommentCommandService;
import com.blog.be.comment.presentation.dto.CommentCreateRequest;
import com.blog.be.comment.presentation.dto.CommentUpdateRequest;
import com.blog.be.comment.presentation.dto.ReplyCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> createRootComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request
    ) {
        commentCommandService.createRootComment(postId, request.content());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments/{parentId}/replies")
    public ResponseEntity<Void> createReply(
            @PathVariable Long parentId,
            @RequestBody ReplyCreateRequest request
    ) {
        commentCommandService.createReply(
                parentId,
                request.postId(),
                request.content()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long id,
            @RequestBody CommentUpdateRequest request
    ) {
        commentCommandService.updateComment(id, request.content());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id
    ) {
        commentCommandService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }
}