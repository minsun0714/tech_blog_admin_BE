package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImageJpaEntity, Long> {

    List<PostImageJpaEntity> findAllByPostId(Long postId);

    void deleteAllByPostId(Long postId);
}
