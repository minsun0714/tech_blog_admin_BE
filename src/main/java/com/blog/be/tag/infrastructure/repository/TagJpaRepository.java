package com.blog.be.tag.infrastructure.repository;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, Long> {

    Set<TagJpaEntity> findAllByNameIn(Set<String> tagNames);

    boolean existsByName(String name);
}
