package com.blog.be.post.presentation.dto;

import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.domain.image.PostImage;

import java.util.List;
import java.util.Set;

public record PostUpdateRequest(
        String title,
        String content,
        Set<String> tagNames,
        Long categoryId,
        Long seriesId,
        PublishStatus publishStatus
) {
}
