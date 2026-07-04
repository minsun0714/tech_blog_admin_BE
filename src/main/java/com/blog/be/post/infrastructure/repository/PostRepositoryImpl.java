package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostRepository;
import com.blog.be.post.domain.PostTagRepository;
import com.blog.be.post.infrastructure.persistence.PostImageJpaEntity;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import com.blog.be.post.infrastructure.persistence.PostTagJpaEntity;
import com.blog.be.post.infrastructure.persistence.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostImageJpaRepository postImageJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostTagRepository postTagRepository;

    @Override
    public Post save(Post post) {

        PostJpaEntity entity = PostMapper.toJpaEntity(post);

        PostJpaEntity savedPostEntity = postJpaRepository.save(entity);

        // 수정일 경우 기존 이미지 삭제
        if (!Objects.isNull(post.getPostId())) {
            postImageJpaRepository.deleteAllByPostId(savedPostEntity.getId());
        }

        return PostMapper.toDomain(savedPostEntity, post.getTagIds());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        Set<Long> tagIds = postTagRepository.findAllByPostId(postId)
                .stream()
                .map(PostTagJpaEntity::getTagId)
                .collect(Collectors.toSet());

        return postJpaRepository.findById(postId)
                .map(postJpaEntity -> PostMapper.toDomain(postJpaEntity, tagIds));
    }

    @Override
    public List<Post> findAllByCategoryId(Long categoryId) {
        return postJpaRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Post> findAllBySeriesId(Long seriesId) {
        return postJpaRepository.findAllBySeriesId(seriesId);
    }

    @Override
    public void delete(Post post) {
        postImageJpaRepository.deleteAllByPostId(post.getPostId());
        postJpaRepository.delete(PostMapper.toJpaEntity(post));
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }
}