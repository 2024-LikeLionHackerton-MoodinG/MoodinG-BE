package com.likelion.mooding.feedback.exception;

public class FeedbackNotFoundException extends RuntimeException {

    public FeedbackNotFoundException() {
        super("Feedback not found");
    }
}
