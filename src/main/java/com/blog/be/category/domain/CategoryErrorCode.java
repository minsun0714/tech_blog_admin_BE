package com.blog.be.category.domain;

import com.blog.be.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CategoryErrorCode implements ErrorCode {

    CATEGORY_NAME_BLANK(
            HttpStatus.BAD_REQUEST,
            "카테고리 이름은 비어 있을 수 없습니다."
    ),

    CATEGORY_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "카테고리를 찾을 수 없습니다."
    ),

    CATEGORY_HAS_CHILDREN(
            HttpStatus.BAD_REQUEST,
            "하위 카테고리가 존재합니다."
    ),

    CATEGORY_HAS_POSTS(
            HttpStatus.BAD_REQUEST,
            "게시글이 존재하는 카테고리는 삭제할 수 없습니다."
    );

    private final HttpStatus status;
    private final String message;

    CategoryErrorCode(HttpStatus status, String message) {
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