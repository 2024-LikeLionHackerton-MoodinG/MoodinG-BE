package com.likelion.mooding.infrastructure.openai.dto;

import com.likelion.mooding.infrastructure.openai.constant.ChatCompletionRole;
import java.util.List;

public record ChatCompletionCreateRequest(
        String model,
        List<ChatCompletionMessage> messages
) {

    public static ChatCompletionCreateRequest from(final String userContent) {
        return new ChatCompletionCreateRequest(
                "gpt-3.5-turbo",
                List.of(
                        // TODO: system, user, assistant 추가
                        new ChatCompletionMessage(ChatCompletionRole.SYSTEM.getRole(),
                                                  "너는 이름은 무딩이야. 사용자가 너에게 감정일기를 제공할텐데, 너는 이 일기 내용을 그저 공감해주면 돼. 이유는 절대로 묻지마. 최대한 따뜻한 어조를 사용해서 답변해줘."),
                        new ChatCompletionMessage(ChatCompletionRole.USER.getRole(), userContent)
                       )
        );
    }
}
