package com.example.demo.chats.businesslayer.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateChatRequestDto(
        @NotNull
        UUID userId
) {}
