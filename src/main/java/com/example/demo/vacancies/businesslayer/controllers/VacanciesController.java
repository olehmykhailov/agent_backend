package com.example.demo.vacancies.businesslayer.controllers;

import com.example.demo.vacancies.businesslayer.dtos.CreateVacancyRequestDto;
import com.example.demo.vacancies.businesslayer.dtos.VacancyGetResponseDto;
import com.example.demo.vacancies.businesslayer.services.VacanciesService;
import com.example.demo.globals.PageResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacanciesController {
    private final VacanciesService vacanciesService;

    @GetMapping("/{chatId}")
    public PageResponseDto<VacancyGetResponseDto> getVacancies(
            @PathVariable UUID chatId,
            @RequestParam int page,
            @RequestParam int size) {
        return vacanciesService.getVacancies(chatId, page, size);
    }

    @PostMapping("/{chatId}")
    public void createVacancy(
            @PathVariable UUID chatId,
            @RequestBody List<CreateVacancyRequestDto> createVacancyRequestDtos
    ) {
        vacanciesService.saveVacancies(chatId, createVacancyRequestDtos);
    }
}
