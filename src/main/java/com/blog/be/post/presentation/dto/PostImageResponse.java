package com.blog.be.post.presentation.dto;

public record PostImageResponse(
        String imageUrl
) {
    public static PostImageResponse of(String imageUrl) {
        return new PostImageResponse(imageUrl);
    }
}
