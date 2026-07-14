package com.blog.be.comment.presentation.dto;

public record CommentCreateRequest(
		String author,
		String password,
        String content
) {
}
