package com.likelion.mooding.infrastructure.openai.dto;

public record ChatCompletionMessage(
        String role,
        String content
) {
}
