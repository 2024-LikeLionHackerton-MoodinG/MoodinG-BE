package com.likelion.mooding.common.exception;

public class CommonException extends BaseException {

    private final CommonExceptionType exceptionType;

    public CommonException(final CommonExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
