package com.blog.be.comment.infrastructure.repository;

import com.blog.be.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
