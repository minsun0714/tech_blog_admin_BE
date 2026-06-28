package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
}
