package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostRepository;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import com.blog.be.post.infrastructure.persistence.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.blog.be.post.infrastructure.persistence.mapper.PostMapper.toJpaEntity;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public PostJpaEntity save(Post post) {
        return postJpaRepository.save(toJpaEntity(post));
    }

    @Override
    public Optional<PostJpaEntity> findById(Long postId) {
        return postJpaRepository.findById(postId);
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(toJpaEntity(post));
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }
}
