package com.blog.be.post.domain;

import com.blog.be.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "해당 게시물이 존재하지 않습니다."
    ),

    DUPLICATE_POST_IMAGE(
            HttpStatus.BAD_REQUEST,
            "이미 추가된 이미지입니다."
    ),

    POST_IMAGE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "게시물에 해당 이미지가 존재하지 않습니다."
    ),

    INVALID_POST_TITLE(
            HttpStatus.BAD_REQUEST,
            "게시물 제목은 비어 있을 수 없습니다."
    ),

    INVALID_POST_CONTENT(
            HttpStatus.BAD_REQUEST,
            "게시물 내용은 비어 있을 수 없습니다."
    );

    private final HttpStatus status;
    private final String message;

    PostErrorCode(HttpStatus status, String message) {
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