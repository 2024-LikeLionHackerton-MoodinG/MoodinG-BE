package com.likelion.mooding.infrastructure.openai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

    // TODO: OpenAI API Key 숨기기
//    @Value("${openai.api.key}")
    private final String OPENAI_API_KEY = "";

    @Bean
    public WebClient openAIClient() {
        return WebClient
                .builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + OPENAI_API_KEY)
                .build();
    }
}
