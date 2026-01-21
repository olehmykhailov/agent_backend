package com.example.demo.vacancies.businesslayer.controllers;

import com.example.demo.vacancies.businesslayer.dtos.VacanciesUpdateEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class VacanciesWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public VacanciesWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // метод для уведомления фронта о новых вакансиях
    public void sendVacanciesUpdate(UUID chatId, int count) {
        messagingTemplate.convertAndSend(
                "/topic/vacancies/" + chatId,
                new VacanciesUpdateEvent(chatId, count)
        );
    }
}
