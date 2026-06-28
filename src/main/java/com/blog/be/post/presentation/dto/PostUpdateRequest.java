package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.image.PostImage;

import java.util.List;
import java.util.Set;

public record PostUpdateRequest(
        String title,
        String content,
        List<PostImage> postImages,
        Set<String> tagNames,
        Long categoryId,
        Long seriesId
) {
}
