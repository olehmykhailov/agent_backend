package com.example.demo.amq.dtos.response.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum FieldsEnum {
    JOB_TITLE("job_title", "jobTitle"),
    LOCATION("location",  "location"),
    MIN_SALARY("min_salary",  "minSalary"),
    MAX_SALARY("max_salary",   "maxSalary"),
    TECH_STACK("tech_stack",  "techStack"),
    DESCRIPTION("description",  "description"),
    COMPANY_NAME("company_name",  "companyName"),
    SENIORITY_LEVEL("seniority_level", "seniorityLevel"),
    SOURCE_LINK("source_link",   "sourceLink"),
    EXPIRE_AT("expire_at", "expireAt"),;

    private final String jsonValue; // для Python/RabbitMQ
    @Getter
    private final String entityField; // для JPA запросов

    FieldsEnum(String jsonValue, String entityField) {
        this.jsonValue = jsonValue;
        this.entityField = entityField;
    }

    @JsonValue // Указывает Jackson использовать это строковое значение для JSON
    public String getJsonValue() {
        return jsonValue;
    }
}