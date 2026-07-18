package com.blog.be.comment.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record CommentUpdateRequest(
		@NotNull String author,
		@NotNull String password,
        @NotNull String content
) {
}
