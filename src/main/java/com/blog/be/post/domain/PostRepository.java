package com.blog.be.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageable);

    Page<Post> findAllBySeriesId(Long seriesId, Pageable pageable);

    void delete(Post post);

    boolean existsById(Long postId);
}