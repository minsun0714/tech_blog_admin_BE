package com.blog.be.post.domain;

import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import com.blog.be.post.infrastructure.repository.projection.PostTagName;

import java.util.List;
import java.util.Set;

public interface PostTagRepository {

    void saveAll(Long postId, Set<Long> tagIds);

    Set<PostTagJpaEntity> findAllByPostId(Long postId);

    Set<PostTagJpaEntity> findAllByPostIds(Set<Long> postIds);

    Set<PostTagJpaEntity> findAllByTagId(Long tagId);

    List<PostTagName> findNamesByPostIds(Set<Long> postIds);

    void deleteAllByPostId(Long postId);
}
