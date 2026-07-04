package com.blog.be.series.application;

import com.blog.be.series.domain.SeriesRepository;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeriesQueryService {

    private final SeriesRepository seriesRepository;

    public List<SeriesJpaEntity> findAll() {
        return seriesRepository.findAll();
    }
}
