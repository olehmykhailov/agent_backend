package com.example.demo.messages.businesslayer.controllers;

import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;
import com.example.demo.globals.PageResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("{chatId}")
    public PageResponseDto<MessageGetResponseDto> getMessagesByChatId(
            @PathVariable("chatId") UUID chatId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "50", name = "size") int size
    ) {
        return messageService.getMessagesByChatId(chatId, page, size);
    }
}
