package com.example.demo.chats.businesslayer.services;


import com.example.demo.chats.businesslayer.dtos.*;
import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.chats.datalayer.repositories.ChatRepository;
import com.example.demo.infrastructure.security.SecurityService;
import com.example.demo.users.businesslayer.services.UsersService;
import com.example.demo.users.datalayer.entities.UserEntity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UsersService usersService;
    private final SecurityService securityService;

    public ChatService(ChatRepository chatRepository, UsersService usersService,  SecurityService securityService) {
        this.chatRepository = chatRepository;
        this.usersService = usersService;
        this.securityService = securityService;
    }

    @Transactional
    public CreateChatResponseDto createChat() {
        ChatEntity chatEntity = new ChatEntity();
        UUID currentUserId = securityService.getCurrentUserId();
        UserEntity user = usersService.getUserById(currentUserId);
        chatEntity.setUser(user);
        chatEntity.setTitle("New chat");
        chatEntity.setTitleGenerated(false);
        ChatEntity createdChat =  chatRepository.save(chatEntity);
        return new CreateChatResponseDto(
                createdChat.getId(),
                createdChat.getTitle(),
                createdChat.isTitleGenerated()
        );
    }

    public Page<ChatGetResponseDto> getUserChats(UUID userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<ChatEntity> chatPage = chatRepository.findAllByUserIdOrderByUpdatedAtDesc(userId, pageable);


        // Преобразуем сущности в DTO
        return chatPage.map(chat -> new ChatGetResponseDto(
                chat.getId(),
                chat.getUser().getId(),
                chat.getTitle()
        ));
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
