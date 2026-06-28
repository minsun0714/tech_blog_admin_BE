package com.blog.be.tag.domain;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;

import java.util.Optional;

public interface TagRepository {

    TagJpaEntity save(TagJpaEntity tag);

    Optional<TagJpaEntity> findById(Long tagId);

    Optional<TagJpaEntity> findByName(String name);

    boolean existsByName(String name);

    void delete(TagJpaEntity tag);
}