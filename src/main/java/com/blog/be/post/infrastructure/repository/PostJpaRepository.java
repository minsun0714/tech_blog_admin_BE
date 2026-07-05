package com.blog.be.post.infrastructure.repository;

import com.blog.be.post.infrastructure.persistence.PostJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {

    Page<PostJpaEntity> findAllByCategoryId(Long categoryId, Pageable pageable);

    Page<PostJpaEntity> findAllBySeriesId(Long seriesId, Pageable pageable);

    Page<PostJpaEntity> findAllByIdIn(List<Long> postIds, Pageable pageable);
}
