package com.blog.be.comment.domain;

import com.blog.be.common.exception.BaseException;

public class CommentException extends BaseException {
    public CommentException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
