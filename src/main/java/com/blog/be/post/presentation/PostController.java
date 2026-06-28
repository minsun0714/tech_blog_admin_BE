package com.blog.be.post.presentation;

import com.blog.be.post.application.PostService;
import com.blog.be.post.presentation.dto.PostDraftRequest;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/publish")
    public ResponseEntity<Void> publishPost(
            @RequestBody PostPublishRequest request
    ) {
        postService.publishPost(
                request.title(),
                request.content(),
                request.postImages(),
                request.tagNames(),
                request.categoryId(),
                request.seriesId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/draft")
    public ResponseEntity<Void> draftPost(
            @RequestBody PostDraftRequest request
    ) {
        postService.draftPost(
                request.title(),
                request.content(),
                request.postImages(),
                request.tagNames(),
                request.categoryId(),
                request.seriesId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request
    ) {
        postService.updatePost(
                postId,
                request.title(),
                request.content(),
                request.postImages(),
                request.tagNames(),
                request.categoryId(),
                request.seriesId()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }
}