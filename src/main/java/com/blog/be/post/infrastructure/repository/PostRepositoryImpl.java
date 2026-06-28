package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.Post;
import com.blog.be.post.domain.PostId;
import com.blog.be.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;


    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public Optional<Post> findById(PostId postId) {
        return postJpaRepository.findById(postId.id());
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(post);
    }

    @Override
    public boolean existsById(PostId postId) {
        return postJpaRepository.existsById(postId.id());
    }
}
