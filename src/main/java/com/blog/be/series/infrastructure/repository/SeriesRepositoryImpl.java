package com.blog.be.series.infrastructure.repository;

import com.blog.be.series.domain.SeriesRepository;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SeriesRepositoryImpl implements SeriesRepository {

    private final SeriesJpaRepository seriesJpaRepository;

    @Override
    public SeriesJpaEntity save(SeriesJpaEntity series) {
        return seriesJpaRepository.save(series);
    }

    @Override
    public Page<SeriesResponseDto> findAll(Pageable pageable) {
        return seriesJpaRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Optional<SeriesJpaEntity> findById(Long id) {
        return seriesJpaRepository.findById(id);
    }

    @Override
    public void delete(SeriesJpaEntity series) {
        seriesJpaRepository.delete(series);
    }

    @Override
    public boolean existsById(Long seriesId) {
        return seriesJpaRepository.existsById(seriesId);
    }
}
