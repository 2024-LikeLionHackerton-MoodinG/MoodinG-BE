package com.likelion.mooding.feedback.application;

import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackResultResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackStatusResponse;
import com.likelion.mooding.feedback.domain.Feedback;
import com.likelion.mooding.feedback.domain.FeedbackRepository;
import com.likelion.mooding.feedback.domain.FeedbackStatus;
import com.likelion.mooding.feedback.exception.FeedbackException;
import com.likelion.mooding.feedback.exception.FeedbackExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class FeedbackService {

    private final FeedbackChatCompletionService feedbackChatCompletionService;

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(final FeedbackChatCompletionService feedbackChatCompletionService,
                           final FeedbackRepository feedbackRepository) {
        this.feedbackChatCompletionService = feedbackChatCompletionService;
        this.feedbackRepository = feedbackRepository;
    }

    public Long createFeedback(final Guest guest,
                               final FeedbackCreateRequest request) {
        final Feedback feedback = new Feedback(FeedbackStatus.IN_PROGRESS, "", guest);
        feedbackRepository.save(feedback);

        final Mono<FeedbackCreateResponse> mono = feedbackChatCompletionService.completeChat(
                request);
        mono.subscribe(
                response -> feedbackRepository.updateFeedbackStatusAndContentById(feedback.getId(),
                        FeedbackStatus.DONE,
                        response.feedback()));
        return feedback.getId();
    }

    public FeedbackStatusResponse getFeedbackStatus(final Guest guest,
                                                    final Long id) {
        final Feedback feedback = feedbackRepository.findByIdOrThrow(id);
        if (!feedback.isOwner(guest)) {
            throw new FeedbackException(FeedbackExceptionType.NOT_FOUND_FEEDBACK);
        }
        return new FeedbackStatusResponse(feedback.getFeedbackStatus().name());
    }

    public FeedbackResultResponse getFeedback(final Guest guest,
                                              final Long id) {
        final Feedback feedback = feedbackRepository.findByIdOrThrow(id);
        if (!feedback.isOwner(guest)) {
            throw new FeedbackException(FeedbackExceptionType.NOT_FOUND_FEEDBACK);
        }
        return new FeedbackResultResponse(feedback.getContent());
    }
}
