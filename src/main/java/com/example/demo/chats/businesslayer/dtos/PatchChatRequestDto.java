package com.example.demo.chats.businesslayer.dtos;

public record PatchChatRequestDto(
        String title,
        boolean titleGenerated
) {
}
