package com.example.demo.responses.businesslayer.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


public record CreateResponseRequestDto (
        @NotBlank
        UUID chatId,
        @NotBlank
        UUID messageId,

        @NotBlank
        String content

) {
}
