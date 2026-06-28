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

        List<PostImageJpaEntity> postImageJpaEntities = PostMapper.toPostImageJpaEntities(post);

        List<PostImageJpaEntity> savedPostImages = postImageJpaRepository.saveAll(postImageJpaEntities);

        return PostMapper.toDomain(savedPostEntity, savedPostImages, post.getTagIds());
    }

    @Override
    public Optional<Post> findById(Long postId) {
        List<PostImageJpaEntity> postImageJpaEntities = postImageJpaRepository.findAllByPostId(postId);
        Set<Long> tagIds = postTagRepository.findAllByPostId(postId)
                .stream()
                .map(PostTagJpaEntity::getTagId)
                .collect(Collectors.toSet());

        return postJpaRepository.findById(postId)
                .map(postJpaEntity -> PostMapper.toDomain(postJpaEntity, postImageJpaEntities, tagIds));
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(PostMapper.toJpaEntity(post));
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }
}