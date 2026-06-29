package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostTagJpaRepository extends JpaRepository<PostTagJpaEntity, Long> {

    Set<PostTagJpaEntity> findAllByPostId(Long postId);

    Set<Long> deleteAllByPostId(Long postId);
}
