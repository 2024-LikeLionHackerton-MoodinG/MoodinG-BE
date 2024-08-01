package com.likelion.mooding.common.exception;

public record ExceptionResponse(
        int HttpStatusCode,
        String errorCode,
        String errorMessage
) {
}
