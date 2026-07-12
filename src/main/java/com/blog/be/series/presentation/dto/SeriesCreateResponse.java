package com.blog.be.series.presentation.dto;

public record SeriesCreateResponse (
        long id
) {
    public static SeriesCreateResponse of(long seriesId) {
        return new SeriesCreateResponse(seriesId);
    }
}
