package com.blog.be.series.domain;

import lombok.Getter;

import java.util.Objects;

public class Series {

    private SeriesId id;

    @Getter
    private String name;

    private Series(String name) {
        this.name = name;
    }

    public static Series create(String name) {
        Objects.requireNonNull(name);
        validateName(name);

        return new Series(name);
    }

    public void changeName(String name) {
        Objects.requireNonNull(name);

        validateName(name);

        this.name = name;
    }

    private static void validateName(String name) {
        if (name.isBlank()) {
            throw new SeriesException(SeriesErrorCode.INVALID_SERIES_NAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(id, series.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
