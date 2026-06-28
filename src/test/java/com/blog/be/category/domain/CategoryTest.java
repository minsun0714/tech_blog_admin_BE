package com.blog.be.category.domain;

import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CategoryTest {

    @Test
    @DisplayName("루트 카테고리를 생성한다.")
    void createRootCategory() {
        // given
        String name = "Backend";

        // when
        CategoryJpaEntity category = CategoryJpaEntity.create(name, null);

        // then
        assertThat(category.isRoot()).isTrue();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("하위 카테고리를 생성한다.")
    void createChildCategory() {
        // given
        Long parentId = 1L;

        // when
        CategoryJpaEntity category = CategoryJpaEntity.create("Spring", parentId);

        // then
        assertThat(category.isRoot()).isFalse();
        assertThat(category.getParentId()).isEqualTo(parentId);
    }

    @Test
    @DisplayName("카테고리 이름을 변경한다.")
    void changeName() {
        // given
        CategoryJpaEntity category = CategoryJpaEntity.create("Backend", null);

        // when
        category.changeName("Java");

        // then
        assertThat(category.getName()).isEqualTo("Java");
    }

    @Test
    @DisplayName("빈 이름으로 변경하면 예외가 발생한다.")
    void changeNameWithBlank() {
        // given
        CategoryJpaEntity category = CategoryJpaEntity.create("Backend", null);

        // when & then
        assertThatThrownBy(() -> category.changeName(""))
                .isInstanceOf(CategoryException.class);
    }

    @Test
    @DisplayName("부모 카테고리를 변경한다.")
    void changeParent() {
        // given
        CategoryJpaEntity category = CategoryJpaEntity.create("Spring", null);
        Long parentId = 1L;

        // when
        category.changeParent(parentId);

        // then
        assertThat(category.getParentId()).isEqualTo(parentId);
        assertThat(category.isRoot()).isFalse();
    }

    @Test
    @DisplayName("부모를 제거하면 루트 카테고리가 된다.")
    void removeParent() {
        // given
        CategoryJpaEntity category = CategoryJpaEntity.create("Spring", 1L);

        // when
        category.changeParent(null);

        // then
        assertThat(category.isRoot()).isTrue();
        assertThat(category.getParentId()).isNull();
    }
}