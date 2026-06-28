package com.blog.be.category.infrastructure.persistence;

import com.blog.be.category.domain.CategoryErrorCode;
import com.blog.be.category.domain.CategoryException;
import com.blog.be.common.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CategoryJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column
    private Long parentId;

    public static CategoryJpaEntity create(String name, Long parentId) {
        validateName(name);
        return CategoryJpaEntity.builder()
                .name(name)
                .parentId(parentId)
                .build();
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    public void changeParent(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isRoot() {
        return parentId == null;
    }

    private static void validateName(String name) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new CategoryException(CategoryErrorCode.CATEGORY_NAME_BLANK);
        }
    }
}