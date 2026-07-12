package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.domain.OpenStatus;
import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {

    Page<PostJpaEntity> findAllByOpenStatus(OpenStatus openStatus, Pageable pageable);

    Page<PostJpaEntity> findAllByCategoryIdAndOpenStatus(Long categoryId, OpenStatus openStatus, Pageable pageable);

    Page<PostJpaEntity> findAllBySeriesIdAndOpenStatus(Long seriesId, OpenStatus openStatus, Pageable pageable);

    Page<PostJpaEntity> findAllByIdIn(List<Long> postIds, Pageable pageable);
}
