package com.blog.be.post.domain;

import com.blog.be.post.infrastructure.persistence.PostJpaEntity;

import java.util.Optional;

public interface PostRepository {

    PostJpaEntity save(Post post);

    Optional<PostJpaEntity> findById(Long postId);

    void delete(Post post);

    boolean existsById(Long postId);
}