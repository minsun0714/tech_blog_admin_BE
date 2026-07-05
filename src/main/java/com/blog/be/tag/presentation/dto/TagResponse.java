package com.blog.be.tag.presentation.dto;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;

public record TagResponse(
        Long id,
        String name
) {
    public static TagResponse of(TagJpaEntity tagJpaEntity) {
        return new TagResponse(tagJpaEntity.getId(), tagJpaEntity.getName());
    }
}
