package com.blog.be.tag.domain;

import com.blog.be.common.exception.BaseException;

public class TagException extends BaseException {
    public TagException(TagErrorCode errorCode) {
        super(errorCode);
    }
}
