package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.OpenStatus;
import com.blog.be.post.domain.Post;

import java.util.List;

public record PostResponseWithUuid(
        Long postId,
        String title,
        String content,
        OpenStatus openStatus,
        List<String> tagNames,
        Long categoryId,
        Long seriesId,
        String postUuid
) {
    public static PostResponseWithUuid of(Post post, List<String> tagNames, String postUuid) {
        return new PostResponseWithUuid(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getOpenStatus(),
                tagNames,
                post.getCategoryId(),
                post.getSeriesId(),
                postUuid
        );
    }
}