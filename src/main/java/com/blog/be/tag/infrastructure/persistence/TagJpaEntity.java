package com.blog.be.tag.infrastructure.persistence;

import com.blog.be.common.infrastructure.persistence.BaseEntity;
import com.blog.be.tag.domain.TagErrorCode;
import com.blog.be.tag.domain.TagException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Table(name = "tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TagJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    public static TagJpaEntity create(String name) {
        validateName(name);

        return TagJpaEntity.builder()
                .name(name)
                .build();
    }

    public void changeName(String name) {
        validateName(name);
        this.name = name;
    }

    private static void validateName(String name) {
        Objects.requireNonNull(name);

        if (name.isBlank()) {
            throw new TagException(TagErrorCode.INVALID_TAG_NAME);
        }
    }
}