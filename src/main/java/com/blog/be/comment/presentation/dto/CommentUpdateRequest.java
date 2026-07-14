package com.blog.be.comment.presentation.dto;

public record CommentUpdateRequest(
		String author,
		String password,
        String content
) {
}
