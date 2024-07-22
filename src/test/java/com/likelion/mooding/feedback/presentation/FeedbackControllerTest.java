package com.likelion.mooding.feedback.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.likelion.mooding.feedback.application.FeedbackChatCompletionService;
import com.likelion.mooding.feedback.application.dto.FeedbackCreateResponse;
import com.likelion.mooding.feedback.presentation.dto.FeedbackCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedbackControllerTest {

    @MockBean
    private FeedbackChatCompletionService feedbackChatCompletionService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    void mockFeedbackChatCompletionService() {
        given(feedbackChatCompletionService.completeChat(any()))
                .willReturn(Mono.just(new FeedbackCreateResponse("TEST_FEEDBACK")).delayElement(Duration.ofSeconds(1)));
    }

    @Test
    void 사용자가_피드백_생성_요청을_하면_피드백_ID_를_응답한다() {
        // given
        final ExtractableResponse<Response> extract = RestAssured.given()
                                                                 .log().all()
                                                                 .when()
                                                                 .post("/session")
                                                                 .then()
                                                                 .log().all()
                                                                 .extract();
        FeedbackCreateRequest request = new FeedbackCreateRequest("TEST_CONTENT");

        // when
        final ExtractableResponse<Response> actualExtract = RestAssured.given()
                                                                       .sessionId(extract.sessionId())
                                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                       .body(request)
                                                                       .log().all()
                                                                       .when()
                                                                       .post("/feedback")
                                                                       .then()
                                                                       .log().all()
                                                                       .extract();

        // then
        assertThat(actualExtract.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualExtract.header("Location")).isEqualTo("/feedback/status/" + 1L);
    }
}
