package com.blog.be.series.application;

import com.blog.be.series.domain.SeriesRepository;
import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;
import com.blog.be.series.infrastructure.repository.projection.SeriesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeriesQueryService {

    private final SeriesRepository seriesRepository;

    public Page<SeriesResponseDto> findAll(Pageable pageable) {
        return seriesRepository.findAll(pageable);
    }
}
