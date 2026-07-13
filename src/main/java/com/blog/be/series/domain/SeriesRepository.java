package com.blog.be.series.domain;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository {

    SeriesJpaEntity save(SeriesJpaEntity series);

    Page<SeriesResponseDto> findAll(Pageable pageable);

    Optional<SeriesJpaEntity> findById(Long id);

    void delete(SeriesJpaEntity series);

    boolean existsById(Long id);
}
