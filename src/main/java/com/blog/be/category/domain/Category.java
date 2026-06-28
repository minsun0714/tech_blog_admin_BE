package com.blog.be.category.domain;

import com.blog.be.post.domain.PostId;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Category {

    private CategoryId id;

    private String name;

    private CategoryId parentId;

    private Category(String name, CategoryId parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    // 리프노드에만 추가 가능
    public static Category create(String name, CategoryId parentId) {
        validateCategoryName(name);

        return new Category(name, parentId);
    }

    public void changeName (String name) {
        validateCategoryName(name);

        this.name = name;
    }

    private static void validateCategoryName(String name) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_NAME_BLANK);
        }
    }

    public boolean isRoot() {
        return Objects.isNull(this.parentId);
    }

    public void changeParent(CategoryId parentId) {
        this.parentId = parentId; // parentId가 null일 경우 1 depth라는 의미
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
