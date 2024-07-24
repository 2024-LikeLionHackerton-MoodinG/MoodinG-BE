package com.likelion.mooding.feedback.domain;

import com.likelion.mooding.auth.presentation.dto.Guest;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private FeedbackStatus feedbackStatus;

    @Column(length = 1500)
    private String content;

    @Embedded
    private Guest guest;

    public Feedback(final Long id,
                    final FeedbackStatus feedbackStatus,
                    final String content,
                    final Guest guest) {
        this.id = id;
        this.feedbackStatus = feedbackStatus;
        this.content = content;
        this.guest = guest;
    }

    public Feedback(final FeedbackStatus feedbackStatus,
                    final String content,
                    final Guest guest) {
        this(null, feedbackStatus, content, guest);
    }

    protected Feedback() {
    }

    public boolean isOwner(final Guest other) {
        return this.guest.equals(other);
    }

    public Long getId() {
        return id;
    }

    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }

    public String getContent() {
        return content;
    }

    public Guest getGuest() {
        return guest;
    }
}
