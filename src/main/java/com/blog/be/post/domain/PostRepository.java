package com.blog.be.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Post save(Post post, String postUuid);

    Long countByPublishStatus(PublishStatus publishStatus);

    Optional<Post> findById(Long postId);

    String findUuidById(Long postId);

    Page<Post> findAllByPublishStatus(PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllByCategoryIdAndPublishStatus(Long categoryId, PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllBySeriesIdAndPublishStatus(Long seriesId, PublishStatus publishStatus, Pageable pageable);

    Page<Post> findAllByTagIdAndPublishStatus(Long tagId, PublishStatus publishStatus, Pageable pageable);

    String deleteById(Long postId);

    boolean existsById(Long postId);
}