package com.likelion.mooding.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 사용자가_세션_생성을_요청하면_세션을_쿠키에_담아_응답한다() {
        // given & when
        final ExtractableResponse<Response> extract = RestAssured.given()
                                                                 .log().all()
                                                                 .when()
                                                                 .post("/session")
                                                                 .then()
                                                                 .log().all()
                                                                 .extract();

        // then
        final String actualSetCookie = extract.header("Set-Cookie");
        final String actualSessionId = extract.sessionId();
        final int actualStatusCode = extract.statusCode();

        assertThat(actualSetCookie).isEqualTo("JSESSIONID=" + actualSessionId + "; Path=/; HttpOnly");
        assertThat(actualStatusCode).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 기존에_세션이_있던_사용자가_세션을_재요청하면_아무런_행동을_하지_않는다() {
        // given
        final ExtractableResponse<Response> extract = RestAssured.given()
                                                                 .log().all()
                                                                 .when()
                                                                 .post("/session")
                                                                 .then()
                                                                 .log().all()
                                                                 .extract();

        // when
        final ExtractableResponse<Response> secondExtract = RestAssured.given()
                                                                       .sessionId(extract.sessionId())
                                                                       .log().all()
                                                                       .when()
                                                                       .post("/session")
                                                                       .then()
                                                                       .log().all()
                                                                       .extract();

        final String actualSetCookie = secondExtract.header("Set-Cookie");

        // then
        // TODO: 자세한 세션 유효기간 검증 필요
        assertThat(actualSetCookie).isNull();
    }
}
