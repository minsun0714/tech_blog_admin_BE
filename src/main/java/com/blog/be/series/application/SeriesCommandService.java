package com.blog.be.series.application;

import com.blog.be.series.domain.SeriesErrorCode;
import com.blog.be.series.domain.SeriesException;
import com.blog.be.series.domain.SeriesRepository;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SeriesCommandService {

    private final SeriesRepository seriesRepository;

    public Long createSeries(String name) {
        SeriesJpaEntity series = SeriesJpaEntity.create(name);
        SeriesJpaEntity savedSeries = seriesRepository.save(series);
        return savedSeries.getId();
    }

    public void updateSeriesName(Long id, String newName) {
        SeriesJpaEntity series = getSeries(id);

        series.changeName(newName);
    }

    public void deleteSeries(Long id){
        SeriesJpaEntity series = getSeries(id);

        seriesRepository.delete(series);
    }

    private SeriesJpaEntity getSeries(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new SeriesException(SeriesErrorCode.SERIES_NOT_FOUND));
    }
}
