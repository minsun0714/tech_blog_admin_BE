package com.blog.be.post.application.dto;

public record PostCountResponse(
        long publishedPostCount,
        long draftedPostCount
) {
    public static PostCountResponse of(long publishedPostCount, long draftedPostCount) {
        return new PostCountResponse(publishedPostCount, draftedPostCount);
    }
}
