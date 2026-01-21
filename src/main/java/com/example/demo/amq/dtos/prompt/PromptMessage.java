package com.example.demo.amq.dtos.prompt;

import com.example.demo.messages.businesslayer.dtos.Message;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PromptMessage(
        UUID chatId,
        List<Message> messages
) {
}
