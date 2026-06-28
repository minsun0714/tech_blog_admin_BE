package com.blog.be.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    public String getMessage() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}