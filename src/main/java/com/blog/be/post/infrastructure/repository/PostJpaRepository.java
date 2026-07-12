package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.PublishStatus;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {

    Long countByPublishStatus(PublishStatus publishStatus);

    Page<PostJpaEntity> findAllByPublishStatus(PublishStatus publishStatus, Pageable pageable);

    Page<PostJpaEntity> findAllByCategoryIdAndPublishStatus(Long categoryId, PublishStatus publishStatus, Pageable pageable);

    Page<PostJpaEntity> findAllBySeriesIdAndPublishStatus(Long seriesId, PublishStatus publishStatus, Pageable pageable);

    Page<PostJpaEntity> findAllByIdIn(List<Long> postIds, Pageable pageable);
}
