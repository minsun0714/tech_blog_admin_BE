package com.blog.be.post.domain;

import com.blog.be.common.exception.BaseException;

public class PostException extends BaseException {
    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}
