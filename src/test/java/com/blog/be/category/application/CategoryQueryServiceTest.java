package com.blog.be.category.application;

import com.blog.be.category.application.dto.CategoryNode;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CategoryQueryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryQueryService categoryQueryService;

    @Test
    @DisplayName("카테고리를 트리 구조로 반환한다")
    void getAllCategories() {
        // given
        List<CategoryJpaEntity> categories = List.of(
                CategoryJpaEntity.builder()
                        .id(1L)
                        .name("Backend")
                        .parentId(null)
                        .build(),
                CategoryJpaEntity.builder()
                        .id(2L)
                        .name("Spring")
                        .parentId(1L)
                        .build(),
                CategoryJpaEntity.builder()
                        .id(3L)
                        .name("Java")
                        .parentId(1L)
                        .build(),
                CategoryJpaEntity.builder()
                        .id(4L)
                        .name("JPA")
                        .parentId(2L)
                        .build()
        );

        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryNode> result = categoryQueryService.getAllCategories();

        // then
        assertThat(result).hasSize(1);

        CategoryNode root = result.getFirst();
        assertThat(root.getId()).isEqualTo(1L);
        assertThat(root.getChildren()).hasSize(2);

        assertThat(root.getChildren())
                .extracting(CategoryNode::getId)
                .containsExactlyInAnyOrder(2L, 3L);

        CategoryNode spring = root.getChildren().stream()
                .filter(c -> c.getId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(spring.getChildren()).hasSize(1);
        assertThat(spring.getChildren().getFirst().getId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("루트 카테고리가 여러 개이면 모두 반환한다")
    void getAllCategories_multipleRoots() {
        // given
        List<CategoryJpaEntity> categories = List.of(
                CategoryJpaEntity.builder()
                        .id(1L)
                        .name("Backend")
                        .parentId(null)
                        .build(),
                CategoryJpaEntity.builder()
                        .id(2L)
                        .name("Frontend")
                        .parentId(null)
                        .build(),
                CategoryJpaEntity.builder()
                        .id(3L)
                        .name("React")
                        .parentId(2L)
                        .build()
        );

        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<CategoryNode> result = categoryQueryService.getAllCategories();

        // then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(CategoryNode::getId)
                .containsExactlyInAnyOrder(1L, 2L);

        CategoryNode frontend = result.stream()
                .filter(c -> c.getId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(frontend.getChildren())
                .extracting(CategoryNode::getId)
                .containsExactly(3L);
    }
}