package com.likelion.mooding.feedback.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.domain.FeedbackRepository;
import com.likelion.mooding.feedback.domain.FeedbackStatus;
import com.likelion.mooding.feedback.presentation.dto.FeedbackCreateRequest;
import java.time.Duration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class FeedbackServiceTest {

    @MockBean
    private FeedbackChatCompletionService feedbackChatCompletionService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Test
    void 감정일기에_대한_피드백_생성이_완료되면_DB_에_값을_업데이트한다() throws InterruptedException {
        // given
        final FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

        given(feedbackChatCompletionService.completeChat(request))
                .willReturn(Mono.just(new FeedbackCreateResponse("TEST_FEEDBACK")).delayElement(Duration.ofSeconds(2)));

        // when
        final Long feedbackId = feedbackService.createFeedback(new Guest("id"), request);

        // TODO: Thread.sleep 대신 테스트 가능한 방법 찾기
        Thread.sleep(3000);

        // then
        feedbackRepository.findById(feedbackId)
                          .ifPresent(feedback -> {
                              assertThat(feedback.getFeedbackStatus()).isEqualTo(FeedbackStatus.DONE);
                              assertThat(feedback.getContent()).isEqualTo("TEST_FEEDBACK");
                          });
    }
}
