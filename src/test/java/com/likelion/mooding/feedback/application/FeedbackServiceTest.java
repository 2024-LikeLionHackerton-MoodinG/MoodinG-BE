package com.likelion.mooding.feedback.application;

import static com.likelion.mooding.feedback.presentation.FeedbackControllerTest.DURATION_FOR_MONO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.likelion.mooding.auth.presentation.dto.Guest;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.domain.Feedback;
import com.likelion.mooding.feedback.domain.FeedbackRepository;
import com.likelion.mooding.feedback.domain.FeedbackStatus;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackStatusResponse;
import com.likelion.mooding.feedback.exception.FeedbackException;
import java.time.Duration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
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
                .willReturn(Mono.just(new FeedbackCreateResponse("TEST_FEEDBACK"))
                                .delayElement(Duration.ofSeconds(DURATION_FOR_MONO / 1000)));

        // when
        final Long feedbackId = feedbackService.createFeedback(new Guest("id"), request);

        // TODO: Thread.sleep 대신 테스트 가능한 방법 찾기
        Thread.sleep(DURATION_FOR_MONO + 500);

        // then
        feedbackRepository.findById(feedbackId)
                          .ifPresent(feedback -> {
                              assertThat(feedback.getFeedbackStatus()).isEqualTo(
                                      FeedbackStatus.DONE);
                              assertThat(feedback.getContent()).isEqualTo("TEST_FEEDBACK");
                          });
    }

    @Nested
    class 특정_피드백의_진행상태를_요청할_때 {

        final String guestId = "owner_id";
        final String otherId = "other_id";

        @Test
        void 본인의_피드백이라면_진행상태를_응답한다() {
            // given
            final Feedback feedback = new Feedback(FeedbackStatus.IN_PROGRESS,
                    "TEST_CONTENT",
                    new Guest(guestId));
            feedbackRepository.save(feedback);

            // when
            final FeedbackStatusResponse actual = feedbackService.getFeedbackStatus(
                    new Guest(guestId),
                    feedback.getId());

            // then
            final FeedbackStatusResponse expected = new FeedbackStatusResponse(
                    FeedbackStatus.IN_PROGRESS.name());
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        void 본인의_피드백이_아니라면_인가_에러가_발생한다() {
            // given
            final Feedback feedback = new Feedback(FeedbackStatus.IN_PROGRESS,
                    "TEST_CONTENT",
                    new Guest(guestId));
            feedbackRepository.save(feedback);

            // when & then
            assertThatThrownBy(
                    () -> feedbackService.getFeedbackStatus(new Guest(otherId), feedback.getId()))
                    .isInstanceOf(FeedbackException.class);
        }
    }

    @Test
    void 감정일기에_대한_피드백_생성이_완료되면_DB_에_피드백을_가져온다() throws InterruptedException {
        // given
        final FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

        given(feedbackChatCompletionService.completeChat(request))
                .willReturn(Mono.just(new FeedbackCreateResponse("TEST_FEEDBACK"))
                                .delayElement(Duration.ofSeconds(DURATION_FOR_MONO / 1000)));

        // when
        final Long feedbackId = feedbackService.createFeedback(new Guest("id"), request);

        Thread.sleep(DURATION_FOR_MONO + 500);

        // then
        feedbackRepository.findById(feedbackId)
                          .ifPresent(feedback -> {
                              assertThat(feedback.getFeedbackStatus()).isEqualTo(
                                      FeedbackStatus.DONE);
                              assertThat(feedback.getContent()).isEqualTo("TEST_FEEDBACK");
                          });

        assertThat(feedbackService.getFeedback(new Guest("id"), feedbackId).result())
                .isEqualTo("TEST_FEEDBACK");
    }
}
