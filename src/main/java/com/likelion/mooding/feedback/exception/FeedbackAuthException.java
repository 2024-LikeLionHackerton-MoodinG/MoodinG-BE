package com.likelion.mooding.feedback.exception;

public class FeedbackAuthException extends RuntimeException {

    public FeedbackAuthException() {
        super("Feedback auth failed");
    }
}
