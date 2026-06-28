package com.blog.be.post.domain;

import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;

import java.util.Set;

public interface PostTagRepository {

    void saveAll(Long postId, Set<Long> tagIds);

    Set<PostTagJpaEntity> findAllByPostId(Long postId);

    Set<Long> deleteAllByPostId(Long postId);
}
