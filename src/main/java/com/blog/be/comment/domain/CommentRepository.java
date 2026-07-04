package com.blog.be.comment.domain;

import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    CommentJpaEntity save(CommentJpaEntity comment);

    Optional<CommentJpaEntity> findById(Long id);

    List<CommentJpaEntity> findAll();

    void delete(CommentJpaEntity comment);

    boolean existsById(Long id);
}
