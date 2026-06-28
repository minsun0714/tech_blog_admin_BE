package com.blog.be.series.domain;

import java.util.Optional;

public interface SeriesRepository {

    Series save(Series series);

    Optional<Series> findById(SeriesId id);

    void delete(Series series);

    boolean existsById(SeriesId id);
}
