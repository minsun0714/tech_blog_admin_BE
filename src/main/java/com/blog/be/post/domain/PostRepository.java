package com.blog.be.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Post save(Post post, String postUuid);

    Optional<Post> findById(Long postId);

    String findUuidById(Long postId);

    Page<Post> findAllByOpenStatus(PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllByCategoryIdAndOpenStatus(Long categoryId, PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllBySeriesIdAndOpenStatus(Long seriesId, PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllByTagIdAndOpenStatus(Long tagId, PublishStatus publishStatus, Pageable pageable);

    String deleteById(Long postId);

    boolean existsById(Long postId);
}