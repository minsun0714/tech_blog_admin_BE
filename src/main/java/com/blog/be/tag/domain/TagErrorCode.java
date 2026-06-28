package com.blog.be.tag.domain;

import com.blog.be.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum TagErrorCode implements ErrorCode {

    INVALID_TAG_NAME(
            HttpStatus.BAD_REQUEST,
            "태그 이름은 비어 있을 수 없습니다."
    ),

    TAG_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "태그를 찾을 수 없습니다."
    ),

    DUPLICATE_TAG_NAME(
            HttpStatus.CONFLICT,
            "이미 존재하는 태그입니다."
    );

    private final HttpStatus status;
    private final String message;

    TagErrorCode(HttpStatus status, String message) {
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