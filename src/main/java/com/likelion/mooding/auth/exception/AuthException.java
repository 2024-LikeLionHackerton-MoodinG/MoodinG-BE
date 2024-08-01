package com.likelion.mooding.auth.exception;

import com.likelion.mooding.common.exception.BaseException;
import com.likelion.mooding.common.exception.BaseExceptionType;

public class AuthException extends BaseException {

    private final AuthExceptionType exceptionType;

    public AuthException(final AuthExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
