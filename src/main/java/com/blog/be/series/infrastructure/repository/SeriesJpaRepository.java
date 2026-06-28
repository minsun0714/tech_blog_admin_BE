package com.blog.be.series.infrastructure.repository;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesJpaRepository extends JpaRepository<SeriesJpaEntity, Long> {
}
