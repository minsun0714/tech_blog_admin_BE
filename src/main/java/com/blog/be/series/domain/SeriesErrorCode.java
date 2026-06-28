package com.blog.be.series.domain;

import com.blog.be.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SeriesErrorCode implements ErrorCode {

    INVALID_SERIES_NAME(
            HttpStatus.BAD_REQUEST,
            "시리즈 이름은 비어 있을 수 없습니다."
    ),

    SERIES_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "시리즈를 찾을 수 없습니다."
    );

    private final HttpStatus status;
    private final String message;

    SeriesErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}