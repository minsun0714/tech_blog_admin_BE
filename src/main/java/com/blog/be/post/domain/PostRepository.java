package com.blog.be.post.domain;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    List<Post> findAllByCategoryId(Long categoryId);

    List<Post> findAllBySeriesId(Long seriesId);

    void delete(Post post);

    boolean existsById(Long postId);
}