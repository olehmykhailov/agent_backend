package com.example.demo.chats.businesslayer.dtos;

import java.util.UUID;

public record ChatMessageDto(
        UUID chatId,
        UUID userId,
        String content,
        long timestamp
) {}
