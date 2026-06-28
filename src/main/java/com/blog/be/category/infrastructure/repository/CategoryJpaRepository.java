package com.blog.be.category.infrastructure.repository;

import com.blog.be.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
