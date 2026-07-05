package com.blog.be.tag.domain;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {

    TagJpaEntity save(TagJpaEntity tag);

    List<TagJpaEntity> saveAll(Set<TagJpaEntity> tags);

    List<TagJpaEntity> findAll();

    Optional<TagJpaEntity> findById(Long tagId);

    Set<TagJpaEntity> findAllByNameIn(Set<String> tagNames);

    List<TagJpaEntity> findAllByIdIn(Set<Long> tagIds);

    boolean existsByName(String name);

    void delete(TagJpaEntity tag);
}