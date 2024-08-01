package com.likelion.mooding.feedback.exception;

import com.likelion.mooding.common.exception.BaseException;
import com.likelion.mooding.common.exception.BaseExceptionType;

public class FeedbackException extends BaseException {

    private final FeedbackExceptionType exceptionType;

    public FeedbackException(final FeedbackExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
