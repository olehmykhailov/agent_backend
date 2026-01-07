package com.example.demo.chats.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateChatResponseDto(
        @NotBlank
        UUID id,

        @NotBlank
        String title,

        @NotBlank
        boolean titleGenerated
) {}
