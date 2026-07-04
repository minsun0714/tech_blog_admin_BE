package com.blog.be.post.application.dto;

public record DeletedPostImage(
        Long postId,
        boolean isThumbnail,
        String s3Key
) {
    public static DeletedPostImage of(Long postId, boolean isThumbnail, String s3Key) {
        return new DeletedPostImage(postId, isThumbnail, s3Key);
    }
}
