package com.likelion.mooding.infrastructure.openai.dto;

import java.util.List;

public record ChatCompletionCreateResponse(List<ChatCompletionChoice> choices) {

    public String getMessageContent() {
        return choices.get(0)
                      .message()
                      .content();
    }
}
