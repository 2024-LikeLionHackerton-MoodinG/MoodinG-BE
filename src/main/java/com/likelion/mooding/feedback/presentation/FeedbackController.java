package com.likelion.mooding.feedback.presentation;

import com.likelion.mooding.auth.annotation.Auth;
import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.FeedbackService;
import com.likelion.mooding.feedback.presentation.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.presentation.dto.FeedbackStatusResponse;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(final FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Void> createFeedback(@Auth final Guest guest,
                                               @RequestBody final FeedbackCreateRequest request) {
        final Long id = feedbackService.createFeedback(guest, request);
        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/feedback/status/" + id))
                             .build();
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<FeedbackStatusResponse> getFeedbackStatus(@Auth final Guest guest,
                                                                    @PathVariable final Long id) {
        final FeedbackStatusResponse response = feedbackService.getFeedbackStatus(guest, id);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(response);
    }
}
