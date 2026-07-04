package com.blog.be.series.presentation.dto;

import com.blog.be.series.infrastructure.persistence.SeriesJpaEntity;

import java.util.List;

public record SeriesListResponse(
    List<SeriesResponse> seriesResponseList
) {
    public record SeriesResponse(
            Long id,
            String name
    ) {
        public static SeriesResponse from(SeriesJpaEntity seriesJpaEntity) {
            return new SeriesResponse(seriesJpaEntity.getId(), seriesJpaEntity.getName());
        }
    }

    public static SeriesListResponse from(List<SeriesJpaEntity> seriesJpaEntities) {
        List<SeriesResponse> responseList = seriesJpaEntities.stream()
                .map(SeriesResponse::from)
                .toList();

        return new SeriesListResponse(responseList);
    }
}
