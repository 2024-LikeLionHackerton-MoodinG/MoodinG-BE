package com.likelion.mooding.common.exception;

import org.springframework.http.HttpStatus;

public enum CommonExceptionType implements BaseExceptionType {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    ;

    private final HttpStatus httpStatus;

    private final String errorMessage;

    CommonExceptionType(final HttpStatus httpStatus, final String errorMessage) {
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
