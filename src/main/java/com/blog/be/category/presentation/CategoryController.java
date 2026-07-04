package com.blog.be.category.presentation;

import com.blog.be.category.application.CategoryCommandService;
import com.blog.be.category.application.CategoryQueryService;
import com.blog.be.category.application.dto.CategoryNode;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import com.blog.be.category.presentation.dto.CategoryChangeParentRequest;
import com.blog.be.category.presentation.dto.CategoryCreateRequest;
import com.blog.be.category.presentation.dto.CategoryListResponse;
import com.blog.be.category.presentation.dto.CategoryUpdateNameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    private final CategoryCommandService categoryCommandService;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        List<CategoryNode> categories = categoryQueryService.getAllCategories();
        return ResponseEntity.ok(CategoryListResponse.from(categories));
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @RequestBody CategoryCreateRequest request
    ) {
        categoryCommandService.createCategory(
                request.name(),
                request.parentId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> updateCategoryName(
            @PathVariable Long id,
            @RequestBody CategoryUpdateNameRequest request
    ) {
        categoryCommandService.updateCategoryName(id, request.name());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/parent")
    public ResponseEntity<Void> changeParent(
            @PathVariable Long id,
            @RequestBody CategoryChangeParentRequest request
    ) {
        categoryCommandService.changeParent(id, request.parentId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        categoryCommandService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}