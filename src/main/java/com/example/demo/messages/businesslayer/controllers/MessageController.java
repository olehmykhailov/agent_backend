package com.example.demo.messages.businesslayer.controllers;

import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("{chatId}")
    public Page<MessageGetResponseDto> getMessagesByChatId(
            @PathVariable UUID chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        return messageService.getMessagesByChatId(chatId, page, size);
    }
}
