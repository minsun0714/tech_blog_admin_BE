package com.blog.be.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handle(BaseException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }
}
