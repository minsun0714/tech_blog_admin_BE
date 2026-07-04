package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.OpenStatus;
import com.blog.be.post.domain.Post;

import java.util.List;
import java.util.Set;

public record PostListResponse(
        List<PostResponse> postResponseList
) {
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

    public static PostListResponse from(List<Post> postList) {
        return new PostListResponse(
                postList.stream()
                        .map(PostResponse::from)
                        .toList()
        );
    }
}
