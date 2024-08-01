package com.likelion.mooding.common.exception;

import com.likelion.mooding.auth.exception.AuthException;
import com.likelion.mooding.feedback.exception.FeedbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse> handleAuthException(final AuthException exception) {
        log.info(exception.getMessage(), exception);

        final BaseExceptionType exceptionType = exception.exceptionType();

        return ResponseEntity.status(exceptionType.httpStatus())
                             .body(new ExceptionResponse(
                                     exceptionType.httpStatus().value(),
                                     exceptionType.errorCode(),
                                     exceptionType.errorMessage()));
    }

    @ExceptionHandler(FeedbackException.class)
    public ResponseEntity<ExceptionResponse> handleFeedbackException(
            final FeedbackException exception) {
        log.info(exception.getMessage(), exception);

        final BaseExceptionType exceptionType = exception.exceptionType();

        return ResponseEntity.status(exceptionType.httpStatus())
                             .body(new ExceptionResponse(
                                     exceptionType.httpStatus().value(),
                                     exceptionType.errorCode(),
                                     exceptionType.errorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);

        CommonExceptionType exceptionType = CommonExceptionType.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(exceptionType.httpStatus())
                             .body(new ExceptionResponse(
                                     exceptionType.httpStatus().value(),
                                     exceptionType.errorCode(),
                                     exceptionType.errorMessage()));
    }
}
