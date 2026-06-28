package com.blog.be.series.infrastructure;

import com.blog.be.category.domain.Category;
import com.blog.be.category.domain.CategoryId;
import com.blog.be.category.domain.CategoryRepository;
import com.blog.be.category.infrastructure.CategoryJpaRepository;
import com.blog.be.series.domain.Series;
import com.blog.be.series.domain.SeriesId;
import com.blog.be.series.domain.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeriesRepositoryImpl implements SeriesRepository {

    private final SeriesJpaRepository seriesJpaRepository;


    @Override
    public Series save(Series series) {
        return seriesJpaRepository.save(series);
    }

    @Override
    public Optional<Series> findById(SeriesId id) {
        return seriesJpaRepository.findById(id.id());
    }

    @Override
    public void delete(Series series) {
        seriesJpaRepository.delete(series);
    }

    @Override
    public boolean existsById(SeriesId seriesId) {
        return seriesJpaRepository.existsById(seriesId.id());
    }
}
