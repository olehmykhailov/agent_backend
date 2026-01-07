package com.example.demo.amq.dtos;

import java.util.UUID;

public record ResponseMessage(
        UUID chatId,
        UUID messageId,
        String content
) {
}
