package com.example.demo.vacancies.businesslayer.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CreateVacancyRequestDto(
        @JsonProperty("job_title") String jobTitle,
        @JsonProperty("location") String location,
        @JsonProperty("min_salary") Float minSalary,
        @JsonProperty("max_salary") Float maxSalary,
        @JsonProperty("tech_stack") List<String> techStack,
        @JsonProperty("description") String description,
        @JsonProperty("company_name") String companyName,
        @JsonProperty("seniority_level") String seniorityLevel,
        @JsonProperty("source_link") String sourceLink,
        @JsonProperty("expire_at") String expireAt // Используем String, если LocalDate не проглатывает формат
) {
}