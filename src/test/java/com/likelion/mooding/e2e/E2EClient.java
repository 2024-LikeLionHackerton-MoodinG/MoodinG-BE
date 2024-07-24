package com.likelion.mooding.e2e;

import com.likelion.mooding.feedback.presentation.dto.FeedbackCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class E2EClient {

    public static ExtractableResponse<Response> 세션_요청() {
        return RestAssured.given()
                          .log().all()
                          .when()
                          .post("/session")
                          .then()
                          .log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 피드백_생성_요청(final String sessionId,
                                                          final FeedbackCreateRequest request) {
        return RestAssured.given()
                          .sessionId(sessionId)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .body(request)
                          .log().all()
                          .when()
                          .post("/feedback")
                          .then()
                          .log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 피드백_진행상태_요청(final String sessionId,
                                                            final Long id) {
        return RestAssured.given()
                          .sessionId(sessionId)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .log().all()
                          .when()
                          .get("/feedback/status/{id}", id)
                          .then()
                          .log().all()
                          .extract();
    }

    public static ExtractableResponse<Response> 피드백_결과_요청(final String sessionId,
                                                            final Long id) {
        return RestAssured.given()
                          .sessionId(sessionId)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .log().all()
                          .when()
                          .get("/feedback/{id}", id)
                          .then()
                          .log().all()
                          .extract();
    }
}
