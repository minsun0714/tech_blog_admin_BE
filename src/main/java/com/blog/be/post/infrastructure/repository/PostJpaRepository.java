package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.Post;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {
}
