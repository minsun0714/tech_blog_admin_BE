package com.blog.be.post.presentation.dto;

public record PostUuidResponse(
        String postUuid
) {
    public static PostUuidResponse of(String uuid) {
        return new PostUuidResponse(uuid);
    }
}
