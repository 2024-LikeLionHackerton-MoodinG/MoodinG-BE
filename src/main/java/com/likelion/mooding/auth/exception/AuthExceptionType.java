package com.likelion.mooding.auth.exception;

import com.likelion.mooding.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements BaseExceptionType {

    INVALID_SESSION(HttpStatus.UNAUTHORIZED, "유효하지 않은 세션 값입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String errorMessage;

    AuthExceptionType(final HttpStatus httpStatus,
                      final String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorCode() {
        return this.name();
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
