package com.blog.be.category.domain;

import com.blog.be.common.exception.BaseException;

public class CategoryException extends BaseException {
    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode);
    }
}
