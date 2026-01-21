package com.example.demo.amq.dtos.response;

import com.example.demo.messages.businesslayer.dtos.Message;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ResponseMessage(
        @NotBlank
        UUID chatId,
        Message message,
        List<String> errors,
        List<ToolFilter> toolFilters

) {
}
