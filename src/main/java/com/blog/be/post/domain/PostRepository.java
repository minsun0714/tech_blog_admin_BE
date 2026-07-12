package com.blog.be.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Post save(Post post, String postUuid);

    Optional<Post> findById(Long postId);

    String findUuidById(Long postId);

    Page<Post> findAllByOpenStatus(OpenStatus openStatus, Pageable pageable);

    Page<Post> findAllByCategoryIdAndOpenStatus(Long categoryId, OpenStatus openStatus,  Pageable pageable);

    Page<Post> findAllBySeriesIdAndOpenStatus(Long seriesId, OpenStatus openStatus, Pageable pageable);

    Page<Post> findAllByTagIdAndOpenStatus(Long tagId, OpenStatus openStatus, Pageable pageable);

    String deleteById(Long postId);

    boolean existsById(Long postId);
}