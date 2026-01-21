package com.example.demo.amq.dtos.response.toolcall;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record ToolCallDto(
        String id,
        String type,
        FunctionDto function
) {

}
