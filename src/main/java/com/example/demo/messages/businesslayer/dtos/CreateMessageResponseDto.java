package com.example.demo.messages.businesslayer.dtos;

import java.util.UUID;

public record CreateMessageResponseDto(
        UUID id,
        UUID chatId,
        String content
) {
}
