package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.OpenStatus;
import com.blog.be.post.domain.Post;

import java.util.Set;

public record PostResponse(
        Long postId,
        String title,
        String content,
        OpenStatus openStatus,
        Set<Long> tagIds,
        Long categoryId,
        Long seriesId
){
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getOpenStatus(),
                post.getTagIds(),
                post.getCategoryId(),
                post.getSeriesId()
        );
    }

}
