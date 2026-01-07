package com.example.demo.chats.businesslayer.dtos;

import java.util.UUID;

public record PatchChatResponseDto(
        UUID id,
        String title,
        boolean titleGenerated
) {
}
