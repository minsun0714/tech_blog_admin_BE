package com.blog.be.post.domain;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    void delete(Post post);

    boolean existsById(Long postId);
}