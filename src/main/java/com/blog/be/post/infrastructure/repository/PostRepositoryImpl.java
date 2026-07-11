package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.*;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import com.blog.be.post.infrastructure.persistence.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostImageJpaRepository postImageJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostTagRepository postTagRepository;

    @Override
    public Post save(Post post) {
        PostJpaEntity oldJpaEntity = postJpaRepository.findById(post.getPostId())
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        return save(post, oldJpaEntity.getPostUuid());
    }

    @Override
    public Post save(Post post, String postUuid) {

        PostJpaEntity entity = PostMapper.toJpaEntity(post, postUuid);

        PostJpaEntity savedPostEntity = postJpaRepository.save(entity);

        // 수정일 경우 기존 이미지 삭제
        if (!Objects.isNull(post.getPostId())) {
            postImageJpaRepository.deleteAllByPostId(savedPostEntity.getId());
        }

        return PostMapper.toDomain(savedPostEntity, post.getTagIds());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postJpaRepository.findById(postId)
                .map(postJpaEntity -> PostMapper.toDomain(postJpaEntity, getTagIds(postId)));
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return toDomainPage(postJpaRepository.findAll(pageable));
    }

    @Override
    public Page<Post> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return toDomainPage(postJpaRepository.findAllByCategoryId(categoryId, pageable));
    }

    @Override
    public Page<Post> findAllBySeriesId(Long seriesId, Pageable pageable) {
        return toDomainPage(postJpaRepository.findAllBySeriesId(seriesId, pageable));
    }

    @Override
    public Page<Post> findAllByTagId(Long tagId, Pageable pageable) {
        List<Long> postIds = postTagRepository.findAllByTagId(tagId)
                .stream()
                .map(PostTagJpaEntity::getPostId)
                .toList();

        return toDomainPage(postJpaRepository.findAllByIdIn(postIds, pageable));
    }

    @Override
    public void delete(Post post) {
//        postImageJpaRepository.deleteAllByPostId(post.getPostId());
//        postJpaRepository.delete(PostMapper.toJpaEntity(post));
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }

    private Set<Long> getTagIds(Long postId) {
        return postTagRepository.findAllByPostId(postId)
                .stream()
                .map(PostTagJpaEntity::getTagId)
                .collect(Collectors.toSet());
    }

    private Page<Post> toDomainPage(Page<PostJpaEntity> postPage) {
        Set<Long> postIds = postPage.getContent()
                .stream()
                .map(PostJpaEntity::getId)
                .collect(Collectors.toSet());

        Map<Long, Set<Long>> tagIdsByPostId = postIds.isEmpty()
                ? Collections.emptyMap()
                : postTagRepository.findAllByPostIds(postIds)
                .stream()
                .collect(Collectors.groupingBy(
                        PostTagJpaEntity::getPostId,
                        Collectors.mapping(PostTagJpaEntity::getTagId, Collectors.toSet())
                ));

        return postPage.map(postJpaEntity -> PostMapper.toDomain(
                postJpaEntity,
                tagIdsByPostId.getOrDefault(postJpaEntity.getId(), Set.of())
        ));
    }
}