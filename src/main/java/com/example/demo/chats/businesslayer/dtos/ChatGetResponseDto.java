package com.example.demo.chats.businesslayer.dtos;

import java.util.UUID;

public record ChatGetResponseDto(
        UUID id,
        String title
) {}

