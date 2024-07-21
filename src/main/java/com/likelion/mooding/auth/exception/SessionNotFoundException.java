package com.likelion.mooding.auth.exception;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException() {
        super("세션이 존재하지 않습니다.");
    }
}
