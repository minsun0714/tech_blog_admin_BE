package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.OpenStatus;
import com.blog.be.post.domain.Post;

import java.util.List;
import java.util.Set;

public record PostResponse(
        Long postId,
        String title,
        String content,
        OpenStatus openStatus,
        List<String> tagNames,
        Long categoryId,
        Long seriesId
){
    public static PostResponse of(Post post, List<String> tagNames) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getOpenStatus(),
                tagNames,
                post.getCategoryId(),
                post.getSeriesId()
        );
    }
}
