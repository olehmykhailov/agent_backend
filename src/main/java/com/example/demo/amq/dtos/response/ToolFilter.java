package com.example.demo.amq.dtos.response;

import com.example.demo.amq.dtos.response.enums.FieldsEnum;
import com.example.demo.amq.dtos.response.enums.LookupsEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ToolFilter(
        FieldsEnum field,
        LookupsEnum lookup,
        String value
) {
}
