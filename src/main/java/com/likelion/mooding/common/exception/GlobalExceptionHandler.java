package com.likelion.mooding.common.exception;

import com.likelion.mooding.auth.exception.SessionNotFoundException;
import com.likelion.mooding.auth.exception.SessionTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            SessionNotFoundException.class,
            SessionTimeoutException.class})
    public ResponseEntity<ExceptionResponse> handleSessionNotFoundException(final SessionNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(exception.getMessage()));
    }
}
