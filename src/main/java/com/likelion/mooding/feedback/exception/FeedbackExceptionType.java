package com.likelion.mooding.feedback.exception;

import com.likelion.mooding.common.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum FeedbackExceptionType implements BaseExceptionType {

    NOT_FOUND_FEEDBACK(HttpStatus.NOT_FOUND, "피드백이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String errorMessage;

    FeedbackExceptionType(final HttpStatus httpStatus,
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
