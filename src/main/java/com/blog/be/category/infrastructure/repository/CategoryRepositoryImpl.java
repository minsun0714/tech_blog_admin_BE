package com.blog.be.category.infrastructure.repository;

import com.blog.be.category.domain.Category;
import com.blog.be.category.domain.CategoryId;
import com.blog.be.category.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Category save(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return categoryJpaRepository.findById(categoryId.id());
    }

    @Override
    public void delete(Category category) {
        categoryJpaRepository.delete(category);
    }

    @Override
    public boolean existsById(CategoryId categoryId) {
        return categoryJpaRepository.existsById(categoryId.id());
    }

    @Override
    public boolean existsByParentId(CategoryId parentId) {
        return existsById(parentId);
    }
}
