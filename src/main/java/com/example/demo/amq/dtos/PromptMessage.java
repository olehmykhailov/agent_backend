package com.example.demo.amq.dtos;

import java.util.UUID;

public record PromptMessage(
        UUID chatId,
        UUID messageId,
        String content
) {
}
