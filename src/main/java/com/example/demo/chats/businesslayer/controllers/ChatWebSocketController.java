package com.example.demo.chats.businesslayer.controllers;


import com.example.demo.amq.dtos.PromptMessage;
import com.example.demo.messages.businesslayer.dtos.CreateMessageRequestDto;
import com.example.demo.messages.businesslayer.dtos.CreateMessageResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;
import com.example.demo.amq.services.PromptProducer;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;



import java.util.UUID;

@Controller
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final PromptProducer promptProducer;

    public ChatWebSocketController(
            MessageService messageService,
            SimpMessagingTemplate messagingTemplate,
            PromptProducer promptProducer
    ) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.promptProducer = promptProducer;
    }

    // Клиент → /app/chat/{chatId}
    @MessageMapping("/chat/{chatId}")
    public void sendMessage(
            @DestinationVariable UUID chatId,
            CreateMessageRequestDto dto
    ) {
        // userId берётся из SecurityContext / WebSocket session
        CreateMessageResponseDto saved =
                messageService.createMessage(chatId, dto);
        System.out.println(saved.id());
        // сразу рассылаем сообщение пользователя
        messagingTemplate.convertAndSend(
                "/topic/chat/" + chatId,
                saved
        );

        promptProducer.sendPrompt(new PromptMessage(
                saved.chatId(),
                saved.id(),
                saved.content()
                )
        );
    }
}

