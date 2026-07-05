package com.blog.be.post.infrastructure.repository.projection;

public record PostTagName(
        Long postId,
        String tagName
) {
}
