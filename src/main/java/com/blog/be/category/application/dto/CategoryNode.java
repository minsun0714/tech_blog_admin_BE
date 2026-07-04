package com.blog.be.category.application.dto;

import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CategoryNode {
    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryNode> children;

    private CategoryNode(Long id, String name, Long parentId, List<CategoryNode> children) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.children = children;
    }

    public static CategoryNode from(CategoryJpaEntity categoryJpaEntity) {
        return new CategoryNode(
                categoryJpaEntity.getId(),
                categoryJpaEntity.getName(),
                categoryJpaEntity.getParentId(),
                new ArrayList<>()
        );
    }
}

