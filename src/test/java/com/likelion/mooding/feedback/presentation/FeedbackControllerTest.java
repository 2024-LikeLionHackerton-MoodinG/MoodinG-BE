package com.likelion.mooding.feedback.presentation;

import static com.likelion.mooding.e2e.E2EClient.세션_요청;
import static com.likelion.mooding.e2e.E2EClient.피드백_결과_요청;
import static com.likelion.mooding.e2e.E2EClient.피드백_생성_요청;
import static com.likelion.mooding.e2e.E2EClient.피드백_진행상태_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.likelion.mooding.feedback.application.FeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateRequest;
import com.likelion.mooding.feedback.application.dto.FeedbackResultResponse;
import com.likelion.mooding.feedback.application.dto.FeedbackStatusResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeedbackControllerTest {

    public static final int DURATION_FOR_MONO = 2000;

    @MockBean
    private FeedbackChatCompletionService feedbackChatCompletionService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    // TODO: Mono 딜레이 시간을 테스트에 따라 조절할 수 있도록 변경
    @BeforeEach
    void mockFeedbackChatCompletionService() {
        given(feedbackChatCompletionService.completeChat(any()))
                .willReturn(Mono.just(new FeedbackCreateResponse("TEST_FEEDBACK")).delayElement(
                        Duration.ofSeconds(DURATION_FOR_MONO / 1000)));
    }

    @Test
    void 사용자가_피드백_생성_요청을_하면_피드백_ID_를_응답한다() {
        // given
        final ExtractableResponse<Response> sessionResponse = 세션_요청();

        FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

        // when
        final ExtractableResponse<Response> actualExtract = 피드백_생성_요청(sessionResponse.sessionId(), request);

        // then
        final Long id = getIdFromLocationHeader(actualExtract.header("Location"));

        assertThat(actualExtract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualExtract.header("Location")).isEqualTo("/feedback/status/" + id);
    }

    @Nested
    class 사용자가_피드백_진행상태를_요청하면 {

        @Test
        void 피드백_생성이_완료되지_않았을_때_IN_PROGRESS_를_응답한다() {
            // given
            final ExtractableResponse<Response> sessionResponse = 세션_요청();

            FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

            final ExtractableResponse<Response> feedbackResponse = 피드백_생성_요청(sessionResponse.sessionId(), request);
            final Long id = getIdFromLocationHeader(feedbackResponse.header("Location"));

            // when
            final ExtractableResponse<Response> statusResponse = 피드백_진행상태_요청(sessionResponse.sessionId(), id);

            // then
            final FeedbackStatusResponse actual = statusResponse.as(FeedbackStatusResponse.class);
            assertThat(actual.status()).isEqualTo("IN_PROGRESS");
        }

        @Test
        void 피드백_생성이_완료됐을_때_DONE_을_응답한다() throws InterruptedException {
            // given
            final ExtractableResponse<Response> sessionResponse = 세션_요청();

            FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

            final ExtractableResponse<Response> feedbackResponse = 피드백_생성_요청(sessionResponse.sessionId(), request);
            final Long id = getIdFromLocationHeader(feedbackResponse.header("Location"));

            Thread.sleep(DURATION_FOR_MONO + 500);

            // when
            final ExtractableResponse<Response> statusResponse = 피드백_진행상태_요청(sessionResponse.sessionId(), id);

            // then
            final FeedbackStatusResponse actual = statusResponse.as(FeedbackStatusResponse.class);
            assertThat(actual.status()).isEqualTo("DONE");
        }
    }

    @Test
    void 사용자가_생성된_피드백을_요청했을_때_피드백을_응답한다() throws InterruptedException {
        // given
        final ExtractableResponse<Response> sessionResponse = 세션_요청();

        FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

        final ExtractableResponse<Response> feedbackResponse = 피드백_생성_요청(sessionResponse.sessionId(), request);
        final Long id = getIdFromLocationHeader(feedbackResponse.header("Location"));

        Thread.sleep(DURATION_FOR_MONO + 500);

        // when
        final ExtractableResponse<Response> feedbackResultResponse = 피드백_결과_요청(sessionResponse.sessionId(), id);

        // then
        final FeedbackResultResponse resultResponse = feedbackResultResponse.as(FeedbackResultResponse.class);
        assertThat(resultResponse.result()).isEqualTo("TEST_FEEDBACK");
    }

    private Long getIdFromLocationHeader(final String location) {
        final String[] split = location.split("/");
        return Long.parseLong(split[split.length - 1]);
    }
}
