package com.blog.be.comment.presentation;

import com.blog.be.comment.application.CommentCommandService;
import com.blog.be.comment.application.CommentQueryService;
import com.blog.be.comment.application.dto.CommentNode;
import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import com.blog.be.comment.presentation.dto.CommentCreateRequest;
import com.blog.be.comment.presentation.dto.CommentDeleteRequest;
import com.blog.be.comment.presentation.dto.CommentListResponse;
import com.blog.be.comment.presentation.dto.CommentUpdateRequest;
import com.blog.be.comment.presentation.dto.ReplyCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentQueryService commentQueryService;

    private final CommentCommandService commentCommandService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentListResponse> getCommentList(
            @PathVariable Long postId
    ) {
        List<CommentNode> commentNodes = commentQueryService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(CommentListResponse.from(commentNodes));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> createRootComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentCreateRequest request
    ) {
        commentCommandService.createRootComment(postId, request.author(), request.password(), request.content());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments/{parentId}/replies")
    public ResponseEntity<Void> createReply(
            @PathVariable Long parentId,
            @RequestBody @Valid ReplyCreateRequest request
    ) {
        commentCommandService.createReply(
                parentId,
                request.postId(),
                request.author(),
                request.password(),
                request.content()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        commentCommandService.updateComment(id, request.password(), request.content());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestBody @Valid CommentDeleteRequest request
    ) {
        commentCommandService.deleteComment(id, request.password());

        return ResponseEntity.noContent().build();
    }
}