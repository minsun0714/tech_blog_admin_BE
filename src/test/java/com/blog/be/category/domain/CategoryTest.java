package com.blog.be.category.domain;

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
        Category category = Category.create(name, null);

        // then
        assertThat(category.isRoot()).isTrue();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("하위 카테고리를 생성한다.")
    void createChildCategory() {
        // given
        CategoryId parentId = new CategoryId(1L);

        // when
        Category category = Category.create("Spring", parentId);

        // then
        assertThat(category.isRoot()).isFalse();
        assertThat(category.getParentId()).isEqualTo(parentId);
    }

    @Test
    @DisplayName("카테고리 이름을 변경한다.")
    void changeName() {
        // given
        Category category = Category.create("Backend", null);

        // when
        category.changeName("Java");

        // then
        assertThat(category.getName()).isEqualTo("Java");
    }

    @Test
    @DisplayName("빈 이름으로 변경하면 예외가 발생한다.")
    void changeNameWithBlank() {
        // given
        Category category = Category.create("Backend", null);

        // when & then
        assertThatThrownBy(() -> category.changeName(""))
                .isInstanceOf(CategoryException.class);
    }

    @Test
    @DisplayName("부모 카테고리를 변경한다.")
    void changeParent() {
        // given
        Category category = Category.create("Spring", null);
        CategoryId parentId = new CategoryId(1L);

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
        Category category = Category.create("Spring", new CategoryId(1L));

        // when
        category.changeParent(null);

        // then
        assertThat(category.isRoot()).isTrue();
        assertThat(category.getParentId()).isNull();
    }
}