package com.blog.be.comment.infrastructure.repository;

import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {
}
