package com.blog.be.series.infrastructure.repository;

import com.blog.be.series.domain.SeriesRepository;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryImpl implements SeriesRepository {

    private final SeriesJpaRepository seriesJpaRepository;

    @Override
    public SeriesJpaEntity save(SeriesJpaEntity series) {
        return seriesJpaRepository.save(series);
    }

    @Override
    public List<SeriesJpaEntity> findAll() {
        return seriesJpaRepository.findAllByOrderByCreatedAtDesc();
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
