package com.example.demo.chats.businesslayer.controllers;


import com.example.demo.amq.services.PromptProducer;
import com.example.demo.chats.businesslayer.dtos.*;
import com.example.demo.chats.businesslayer.services.ChatService;
import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.globals.PageResponseDto;

import com.example.demo.messages.businesslayer.dtos.CreateMessageRequestDto;
import com.example.demo.messages.businesslayer.dtos.CreateMessageResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;
import com.example.demo.messages.datalayer.enums.SenderType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SecurityService securityService;
    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Возвращаем 201 вместо 200
    public CreateChatResponseDto createChat(@Valid @RequestBody CreateChatRequestDto createChatRequestDto) {
        return chatService.createChat();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatchChatResponseDto patchChat(@Valid @RequestBody PatchChatRequestDto patchChatRequestDto) {
        return chatService.updateChat(patchChatRequestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<ChatGetResponseDto> getUserChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        UUID currentUserId = securityService.getCurrentUserId();
        return chatService.getUserChats(currentUserId, page, size);
    }

    @PostMapping("/{chatId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMessageResponseDto createMessage(
            @Valid @RequestBody CreateMessageRequestDto createMessageRequestDto,
            @PathVariable("chatId") UUID chatId
    ) {
        return messageService.createMessageFromClient(chatId, createMessageRequestDto.content());
    }
}