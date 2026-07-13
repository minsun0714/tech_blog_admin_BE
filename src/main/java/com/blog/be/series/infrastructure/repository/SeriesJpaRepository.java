package com.blog.be.series.infrastructure.repository;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeriesJpaRepository extends JpaRepository<SeriesJpaEntity, Long> {

    @Query(
            value = "SELECT new com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto(s.id, s.name, COUNT(p)) " +
                    "FROM SeriesJpaEntity s " +
                    "LEFT JOIN PostJpaEntity p ON p.seriesId = s.id " +
                    "GROUP BY s.id, s.name",
            countQuery = "SELECT COUNT(s) FROM SeriesJpaEntity s"
    )
    Page<SeriesResponseDto> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
