package com.blog.be.comment.domain;

import com.blog.be.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CommentErrorCode implements ErrorCode {

    INVALID_COMMENT_CONTENT(
            HttpStatus.BAD_REQUEST,
            "댓글 내용은 비어 있을 수 없습니다."
    ),

    COMMENT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "댓글을 찾을 수 없습니다."
    ),

    PARENT_COMMENT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "부모 댓글을 찾을 수 없습니다."
    ),

    COMMENT_ALREADY_DELETED(
            HttpStatus.BAD_REQUEST,
            "이미 삭제된 댓글입니다."
    );

    private final HttpStatus status;
    private final String message;

    CommentErrorCode(HttpStatus status, String message) {
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