package com.example.demo.messages.businesslayer.dtos;

import java.util.UUID;

public record MessageGetResponseDto(
        UUID id,
        UUID chatId,
        String content
) {
}
