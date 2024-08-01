package com.likelion.mooding.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    HttpStatus httpStatus();

    String errorCode();

    String errorMessage();
}
