package com.likelion.mooding.feedback.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    default Feedback findByIdOrThrow(final Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid feedback id"));
    }

    @Transactional
    @Modifying
    @Query("UPDATE Feedback f SET f.feedbackStatus = :feedbackStatus, f.content = :content WHERE f.id = :id")
    void updateFeedbackStatusAndContentById(final Long id, final FeedbackStatus feedbackStatus,
                                            final String content);
}
