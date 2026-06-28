package com.blog.be.category.domain;

import java.util.Optional;

public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(CategoryId categoryId);

    void delete(Category category);

    boolean existsById(CategoryId categoryId);

    boolean existsByParentId(CategoryId parentId);
}
