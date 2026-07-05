package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.PostTagRepository;
import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import com.blog.be.post.infrastructure.repository.projection.PostTagName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PostTagRepositoryImpl implements PostTagRepository {

    private final PostTagJpaRepository postTagJpaRepository;

    @Override
    public void saveAll(Long postId, Set<Long> tagIds) {
        List<PostTagJpaEntity> postTagJpaEntities = tagIds.stream()
                .map(tagId -> PostTagJpaEntity.create(postId, tagId))
                        .toList();

        postTagJpaRepository.saveAll(postTagJpaEntities);
    }

    @Override
    public Set<PostTagJpaEntity> findAllByPostId(Long postId) {
        return postTagJpaRepository.findAllByPostId(postId);
    }

    @Override
    public Set<PostTagJpaEntity> findAllByPostIds(Set<Long> postIds) {
        return postTagJpaRepository.findAllByPostIdIn(postIds);
    }

    @Override
    public Set<PostTagJpaEntity> findAllByTagId(Long tagId) {
        return postTagJpaRepository.findAllByTagId(tagId);
    }

    @Override
    public List<PostTagName> findNamesByPostIds(Set<Long> postIds) {
        return postTagJpaRepository.findNamesByPostIds(postIds);
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        postTagJpaRepository.deleteAllByPostId(postId);
    }
}
