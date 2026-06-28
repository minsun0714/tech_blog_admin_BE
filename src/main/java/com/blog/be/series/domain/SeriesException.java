package com.blog.be.series.domain;

import com.blog.be.common.exception.BaseException;

public class SeriesException extends BaseException {
    public SeriesException(SeriesErrorCode errorCode) {
        super(errorCode);
    }
}
