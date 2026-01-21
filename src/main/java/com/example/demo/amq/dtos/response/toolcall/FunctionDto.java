package com.example.demo.amq.dtos.response.toolcall;


import java.util.Map;

public record FunctionDto(
        String name,
        Map<String, Object> arguments
) {
}
