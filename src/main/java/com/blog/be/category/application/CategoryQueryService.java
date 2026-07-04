package com.blog.be.category.application;

import com.blog.be.category.application.dto.CategoryNode;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import com.blog.be.category.presentation.dto.CategoryListResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryQueryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryNode> getAllCategories() {
        List<CategoryJpaEntity> categoryJpaEntities = categoryRepository.findAll();

        Map<Long, CategoryNode> categoryMap = categoryJpaEntities.stream()
                .collect(Collectors.toMap(
                        CategoryJpaEntity::getId,
                        CategoryNode::from
                ));

        categoryJpaEntities
                .forEach(categoryJpaEntity -> {
                    CategoryNode current = categoryMap.get(categoryJpaEntity.getId());
                    CategoryNode parent = categoryMap.getOrDefault(categoryJpaEntity.getParentId(), null);

                    if (parent == null) return;
                    parent.getChildren().add(current);
                });

        return categoryMap.values()
                .stream()
                .filter(category -> category.getParentId() == null)
                .toList();
    }
}
