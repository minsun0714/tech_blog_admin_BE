package com.blog.be.comment.infrastructure.repository;

import com.blog.be.comment.infrastructure.persistence.CommentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, Long> {

    List<CommentJpaEntity> findAllByPostId(Long postId);
}
