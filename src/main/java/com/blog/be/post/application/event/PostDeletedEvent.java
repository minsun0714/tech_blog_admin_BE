package com.blog.be.post.application.event;

public record PostDeletedEvent(
        String postUuid
) {
}
