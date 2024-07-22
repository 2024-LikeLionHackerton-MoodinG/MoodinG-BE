package com.likelion.mooding.infrastructure.openai.dto;

import com.likelion.mooding.infrastructure.openai.constant.ChatCompletionRole;

public record ChatCompletionMessage(
        String role,
        String content
) {
}
