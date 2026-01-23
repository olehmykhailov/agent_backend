package com.example.demo.messages.businesslayer.controllers;

import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;
import com.example.demo.globals.PageResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<MessageGetResponseDto> getMessagesByChatId(
            @PathVariable("chatId") UUID chatId,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "50", name = "size") int size
    ) {
        return messageService.getMessagesByChatId(chatId, page, size);
    }
}
