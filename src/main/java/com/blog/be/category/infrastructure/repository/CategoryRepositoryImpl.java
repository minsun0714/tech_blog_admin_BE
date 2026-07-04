package com.blog.be.category.infrastructure.repository;

import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public CategoryJpaEntity save(CategoryJpaEntity category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public Optional<CategoryJpaEntity> findById(Long categoryId) {
        return categoryJpaRepository.findById(categoryId);
    }

    @Override
    public void delete(CategoryJpaEntity category) {
        categoryJpaRepository.delete(category);
    }

    @Override
    public boolean existsById(Long categoryId) {
        return categoryJpaRepository.existsById(categoryId);
    }

    @Override
    public boolean existsByNameAndParentId(String name, Long parentId) {
        if (parentId == null) return categoryJpaRepository.existsByNameAndParentIdIsNull(name);
        return categoryJpaRepository.existsByNameAndParentId(name, parentId);
    }
}
