package com.blog.be.category.presentation.dto;

public record CategoryCreateRequest(
        String name,
        Long parentId
) {
}
