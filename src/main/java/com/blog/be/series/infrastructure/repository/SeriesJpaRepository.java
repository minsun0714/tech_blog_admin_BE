package com.blog.be.series.infrastructure.repository;

import com.blog.be.series.domain.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesJpaRepository extends JpaRepository<Series, Long> {
}
