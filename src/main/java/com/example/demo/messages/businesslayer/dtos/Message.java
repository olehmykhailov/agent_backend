package com.example.demo.messages.businesslayer.dtos;

import com.example.demo.amq.dtos.response.toolcall.ToolCallDto;
import com.example.demo.messages.datalayer.enums.SenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Message(
        SenderType role,
        String content,
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        List<ToolCallDto> toolCalls,
        String toolCallId
) {
}
