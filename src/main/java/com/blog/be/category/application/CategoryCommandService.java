package com.blog.be.category.application;

import com.blog.be.category.domain.CategoryErrorCode;
import com.blog.be.category.domain.CategoryException;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryCommandService {

    private final CategoryRepository categoryRepository;

    public void createCategory(String name, Long parentId) {
        if (categoryRepository.existsByNameAndParentId(name, parentId)) {
            throw new CategoryException(CategoryErrorCode.DUPLICATE_CATEGORY_NAME);
        }
        CategoryJpaEntity category = CategoryJpaEntity.create(name, parentId);
        categoryRepository.save(category);
    }

    public void updateCategoryName(Long id, String newName) {
        CategoryJpaEntity category = getCategory(id);

        category.changeName(newName);
    }

    public void changeParent(Long id, Long parentId) {
        CategoryJpaEntity category = getCategory(id);

        category.changeParent(parentId);
    }

    public void deleteCategory(Long id) {
        CategoryJpaEntity category = getCategory(id);
        
        categoryRepository.delete(category);
    }

    private CategoryJpaEntity getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }
}
