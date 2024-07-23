package com.likelion.mooding.infrastructure.openai.constant;

public enum ChatCompletionRole {

    SYSTEM("system"),

    USER("user"),

    ASSISTANT("assistant");

    private final String role;

    ChatCompletionRole(final String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
