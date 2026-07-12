package com.blog.be.post.presentation;

import com.blog.be.post.application.PostCommandService;
import com.blog.be.post.application.PostQueryService;
import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseWithUuid> getOnePost(
            @PathVariable Long postId
    ) {
        PostResponseWithUuid postResponse = postQueryService.getOnePost(postId);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPublishedPosts(
            @RequestParam(required = false, defaultValue = "PUBLISHED") PublishStatus publishStatus,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long seriesId,
            @RequestParam(required = false) Long tagId,
            @PageableDefault(size = 20, sort = "createdAt", direction = DESC) Pageable pageable
    ) {
        Page<PostResponse> postResponsePage = postQueryService.getPagedPosts(publishStatus, categoryId, seriesId, tagId, pageable);
        return ResponseEntity.ok(postResponsePage);
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
                request.seriesId(),
                request.postUuid()
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
                request.seriesId(),
                request.postUuid()
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