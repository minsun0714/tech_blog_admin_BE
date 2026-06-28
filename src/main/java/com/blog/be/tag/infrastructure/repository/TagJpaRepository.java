package com.blog.be.tag.infrastructure.repository;

import com.blog.be.tag.infrastructure.persistence.TagJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, Long> {

    Optional<TagJpaEntity> findByName(String name);

    boolean existsByName(String name);
}
