package com.likelion.mooding.feedback.application;

import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import reactor.core.publisher.Mono;

public interface FeedbackChatCompletionService {

    Mono<FeedbackCreateResponse> completeChat(final FeedbackCreateRequest request);
}
