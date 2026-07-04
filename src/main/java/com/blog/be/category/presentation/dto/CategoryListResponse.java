package com.blog.be.category.presentation.dto;

import com.blog.be.category.application.CategoryQueryService;
import com.blog.be.category.application.dto.CategoryNode;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record CategoryListResponse(
        List<CategoryResponse> categoryList
) {
    public record CategoryResponse (
            Long categoryId,
            Long parentId,
            String categoryName,
            List<CategoryResponse> childrenCategoryList
    ) {
        private static CategoryResponse from(CategoryNode category) {
            return new CategoryResponse(
                    category.getId(),
                    category.getParentId(),
                    category.getName(),
                    category.getChildren().stream()
                            .map(CategoryResponse::from)
                            .toList()
            );
        }
    }

    public static CategoryListResponse from(List<CategoryNode> categoryList) {
        return new CategoryListResponse(
                categoryList.stream()
                        .map(CategoryResponse::from)
                        .toList()
        );
    }
}
