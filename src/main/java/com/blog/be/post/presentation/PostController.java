package com.blog.be.post.presentation;

import com.blog.be.post.application.PostCommandService;
import com.blog.be.post.application.PostQueryService;
import com.blog.be.post.domain.Post;
import com.blog.be.post.presentation.dto.PostDraftRequest;
import com.blog.be.post.presentation.dto.PostPublishRequest;
import com.blog.be.post.presentation.dto.PostResponse;
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

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getOnePost(
            @PathVariable Long postId
    ) {
        Post post = postQueryService.findById(postId);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<PostResponse>> getAllPostsByCategoryId(
            @RequestParam Long categoryId
    ) {
        List<PostResponse> postResponseList = postQueryService.findByCategoryId(categoryId)
                .stream().map(PostResponse::from)
                .toList();

        return ResponseEntity.ok(postResponseList);
    }

    @GetMapping(params = "seriesId")
    public ResponseEntity<List<PostResponse>> getAllPostsBySeriesId(
            @RequestParam Long seriesId
    ) {
        List<PostResponse> postResponseList = postQueryService.findBySeriesId(seriesId)
                .stream().map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(postResponseList);
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