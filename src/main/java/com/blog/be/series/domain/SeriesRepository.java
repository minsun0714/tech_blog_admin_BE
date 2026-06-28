package com.blog.be.series.domain;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;

import java.util.Optional;

public interface SeriesRepository {

    SeriesJpaEntity save(SeriesJpaEntity series);

    Optional<SeriesJpaEntity> findById(Long id);

    void delete(SeriesJpaEntity series);

    boolean existsById(Long id);
}
