package com.example.demo.vacancies.businesslayer.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;

import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record VacancyGetResponseDto(
        UUID id,
        String jobTitle,
        String location,
        Float minSalary,
        Float maxSalary,
        List<String> techStack,
        String description,
        String companyName,
        String seniorityLevel,
        String sourceLink
) {}