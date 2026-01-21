package com.example.demo.messages.businesslayer.dtos;

import com.example.demo.messages.datalayer.enums.SenderType;

import java.util.UUID;

public record CreateMessageResponseDto(
        UUID id,
        UUID chatId,
        String content,
        SenderType role
) {
}
