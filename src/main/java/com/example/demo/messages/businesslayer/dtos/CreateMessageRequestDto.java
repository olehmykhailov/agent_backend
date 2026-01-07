package com.example.demo.messages.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateMessageRequestDto(
        @NotBlank
        UUID chatId,

        @NotBlank
        String content
) {
}
