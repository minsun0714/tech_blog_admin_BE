package com.blog.be.category.infrastructure.repository;

import com.blog.be.category.infrastructure.persistence.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {
}
