package com.blog.be.comment.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record CommentDeleteRequest (
		@NotNull String password
) {
}
