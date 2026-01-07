package com.example.demo.responses.businesslayer.dtos;

import java.util.UUID;

public record ResponseGetResponseDto(
        UUID id,
        UUID messageId,
        UUID chatId
) {
}
