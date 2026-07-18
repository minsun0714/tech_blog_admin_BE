package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.domain.Post;

import java.util.List;

public record PostResponseWithUuid(
        Long postId,
        String title,
        String content,
        PublishStatus publishStatus,
        List<String> tagNames,
        Long categoryId,
        Long seriesId,
        String postUuid,
        String thumbnailImageUrl
) {
    public static PostResponseWithUuid of(Post post, List<String> tagNames, String postUuid) {
        return new PostResponseWithUuid(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPublishStatus(),
                tagNames,
                post.getCategoryId(),
                post.getSeriesId(),
                postUuid,
                post.getThumbnailImageUrl()
        );
    }
}