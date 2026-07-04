package com.blog.be.post.presentation;

import com.blog.be.post.application.PostCommandService;
import com.blog.be.post.application.PostQueryService;
import com.blog.be.post.domain.Post;
import com.blog.be.post.presentation.dto.PostDraftRequest;
import com.blog.be.post.presentation.dto.PostListResponse;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPostsByCategoryId(
            @RequestParam Long categoryId
    ) {
        List<Post> posts = postQueryService.findByCategoryId(categoryId);
        return ResponseEntity.ok(PostListResponse.from(posts));
    }

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPostsBySeriesId(
            @RequestParam Long seriesId
    ) {
        List<Post> posts = postQueryService.findBySeriesId(seriesId);
        return ResponseEntity.ok(PostListResponse.from(posts));
    }

    @PostMapping("/publish")
    public ResponseEntity<Void> publishPost(
            @RequestBody PostPublishRequest request
    ) {
        postCommandService.publishPost(
                request.title(),
                request.content(),
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
        postCommandService.draftPost(
                request.title(),
                request.content(),
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
        postCommandService.updatePost(
                postId,
                request.title(),
                request.content(),
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
        postCommandService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }
}