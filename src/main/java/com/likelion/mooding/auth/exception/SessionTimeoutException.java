package com.likelion.mooding.auth.exception;

public class SessionTimeoutException extends RuntimeException {

    public SessionTimeoutException() {
        super("세션이 만료되었습니다.");
    }
}
