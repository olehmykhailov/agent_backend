package com.example.demo.chats.businesslayer.controllers;


import com.example.demo.chats.businesslayer.dtos.*;
import com.example.demo.chats.businesslayer.services.ChatService;
import com.example.demo.infrastructure.security.SecurityService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    private final SecurityService securityService;

    public ChatController(ChatService chatService, SecurityService securityService) {
        this.chatService = chatService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Возвращаем 201 вместо 200
    public CreateChatResponseDto createChat(@Valid @RequestBody CreateChatRequestDto createChatRequestDto) {
        // Извлекаем ID текущего пользователя, чтобы никто не мог создать чат от чужого имени

        return chatService.createChat();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatchChatResponseDto patchChat(@Valid @RequestBody PatchChatRequestDto patchChatRequestDto) {
        return chatService.updateChat(patchChatRequestDto);
    }

    @GetMapping
    public Page<ChatGetResponseDto> getUserChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        UUID currentUserId = securityService.getCurrentUserId();
        return chatService.getUserChats(currentUserId, page, size);
    }
}