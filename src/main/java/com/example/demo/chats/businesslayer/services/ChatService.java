package com.example.demo.chats.businesslayer.services;


import com.example.demo.chats.businesslayer.dtos.*;
import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.chats.datalayer.repositories.ChatRepository;
import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.infrastructure.utils.Utils;
import com.example.demo.messages.businesslayer.services.MessageService;
import com.example.demo.messages.datalayer.entities.MessageEntity;
import com.example.demo.messages.datalayer.enums.SenderType;
import com.example.demo.messages.datalayer.repositories.MessagesRepository;
import com.example.demo.users.businesslayer.services.UsersService;
import com.example.demo.users.datalayer.entities.UserEntity;
import com.example.demo.globals.PageResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UsersService usersService;
    private final SecurityService securityService;
    private final MessagesRepository  messagesRepository;
    private final Utils utils;

    @Transactional
    public CreateChatResponseDto createChat() {
        ChatEntity chatEntity = new ChatEntity();
        UUID currentUserId = securityService.getCurrentUserId();
        UserEntity user = usersService.getUserById(currentUserId);
        chatEntity.setUser(user);
        chatEntity.setTitle("New chat");
        chatEntity.setTitleGenerated(false);
        ChatEntity createdChat = chatRepository.save(chatEntity);
        String prompt = "";
        try {
            prompt = utils.getPrompt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChat(createdChat);
        messageEntity.setContent(prompt);
        messageEntity.setRole(SenderType.system);
        messageEntity.setToolCallId(null);
        messageEntity.setToolCalls(null);
        messagesRepository.save(messageEntity);

        return new CreateChatResponseDto(
                createdChat.getId(),
                createdChat.getTitle(),
                createdChat.isTitleGenerated()
        );
    }

    public PageResponseDto<ChatGetResponseDto> getUserChats(UUID userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<ChatEntity> chatPage = chatRepository.findAllByUserIdOrderByUpdatedAtDesc(userId, pageable);


        // Преобразуем сущности в DTO
        Page<ChatGetResponseDto> dtoPage = chatPage.map(chat -> new ChatGetResponseDto(
                chat.getId(),
                chat.getTitle()
        ));

        return new PageResponseDto<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );
    }

    @Transactional
    public PatchChatResponseDto updateChat(PatchChatRequestDto patchChatRequestDto) {
        ChatEntity chatEntity = new ChatEntity();
        UUID currentUserId = securityService.getCurrentUserId();
        UserEntity user = usersService.getUserById(currentUserId);
        chatEntity.setUser(user);
        if (patchChatRequestDto.title() != null) {
            chatEntity.setTitle(patchChatRequestDto.title());
        }

        if (patchChatRequestDto.titleGenerated()) {
            chatEntity.setTitleGenerated(true);
        }

        return new PatchChatResponseDto(
              chatEntity.getId(),
              chatEntity.getTitle(),
              chatEntity.isTitleGenerated()
        );
    }

}
