package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImageJpaEntity, Long> {

    List<PostImageJpaEntity> findAllByPostId(Long postId);

    List<PostImageJpaEntity> deleteAllByPostId(Long postId);

    boolean existsByS3Key(String S3Key);

    PostImageJpaEntity deleteByS3Key(String S3Key);
}
