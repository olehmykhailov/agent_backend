package com.example.demo.vacancies.businesslayer.dtos;

import java.util.UUID;

public record VacanciesUpdateEvent(
        UUID chatId,
        int size
) {
}
