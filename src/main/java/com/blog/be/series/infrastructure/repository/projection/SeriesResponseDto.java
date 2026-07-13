package com.blog.be.series.infrastructure.repository.projection;

public record SeriesResponseDto(
        Long id,
        String name,
        Long postCount
) {
}
