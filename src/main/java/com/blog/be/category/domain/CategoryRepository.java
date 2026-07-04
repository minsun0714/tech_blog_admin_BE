package com.blog.be.category.domain;

import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;

import java.util.Optional;

public interface CategoryRepository {

    CategoryJpaEntity save(CategoryJpaEntity category);

    Optional<CategoryJpaEntity> findById(Long categoryId);

    void delete(CategoryJpaEntity category);

    boolean existsById(Long categoryId);

    boolean existsByNameAndParentId(String name, Long parentId);
}
