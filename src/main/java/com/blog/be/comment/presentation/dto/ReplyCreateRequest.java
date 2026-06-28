package com.blog.be.comment.presentation.dto;

public record ReplyCreateRequest(
        Long postId,
        String content
) {
}
