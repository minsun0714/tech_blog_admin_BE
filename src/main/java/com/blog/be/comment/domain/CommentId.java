package com.blog.be.comment.domain;

import java.util.Objects;

public record CommentId(
        Long id
) {
    public CommentId {
        Objects.requireNonNull(id);
    }
}
