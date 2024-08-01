package com.likelion.mooding.infrastructure.openai.application;

import com.likelion.mooding.feedback.application.FeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.infrastructure.openai.dto.ChatCompletionCreateRequest;
import com.likelion.mooding.infrastructure.openai.dto.ChatCompletionCreateResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAIFeedbackChatCompletionService implements FeedbackChatCompletionService {

    private final WebClient webClient;

    public OpenAIFeedbackChatCompletionService(final WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<FeedbackCreateResponse> completeChat(final FeedbackCreateRequest request) {
        final Mono<ChatCompletionCreateResponse> mono = webClient.post()
                                                                 .accept(MediaType.APPLICATION_JSON)
                                                                 .bodyValue(
                                                                         ChatCompletionCreateRequest.from(
                                                                                 request.diaryContent()))
                                                                 .retrieve()
                                                                 .bodyToMono(
                                                                         ChatCompletionCreateResponse.class);

        return mono.flatMap(response -> {
            final String feedback = response.getMessageContent();
            return Mono.just(new FeedbackCreateResponse(feedback));
        });
    }
}
