package com.example.demo.responses.businesslayer.dtos;

import java.util.UUID;

public record CreateResponseResponseDto(
        UUID id,
        UUID chatId,
        UUID messageId,
        String content
) {
}
