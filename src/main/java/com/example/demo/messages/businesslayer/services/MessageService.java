package com.example.demo.messages.businesslayer.services;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.infrastructure.errors.EntityNotFoundException;
import com.example.demo.messages.businesslayer.MessageMapper;
import com.example.demo.messages.businesslayer.dtos.CreateMessageRequestDto;
import com.example.demo.messages.businesslayer.dtos.CreateMessageResponseDto;
import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.datalayer.entities.MessageEntity;
import com.example.demo.messages.datalayer.repositories.MessagesRepository;
import com.example.demo.chats.datalayer.repositories.ChatRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessagesRepository messagesRepository,  ChatRepository chatRepository, MessageMapper messageMapper) {
        this.messagesRepository = messagesRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
    }

    private MessageGetResponseDto toDto(MessageEntity entity) {
        return new MessageGetResponseDto(
                entity.getId(),
                entity.getChat().getId(),
                entity.getContent()
        );
    }


    @Transactional
    public CreateMessageResponseDto createMessage(UUID chatId, CreateMessageRequestDto createMessageRequestDto) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);

        if (chatEntity == null) {
            throw new EntityNotFoundException("Chat with id " + createMessageRequestDto.chatId() + " not found");
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChat(chatEntity);
        messageEntity.setContent(createMessageRequestDto.content());
        MessageEntity saved = messagesRepository.save(messageEntity);

        return new CreateMessageResponseDto(
                saved.getId(),
                saved.getChat().getId(),
                saved.getContent()
        );

    }

    public Page<MessageGetResponseDto> getMessagesByChatId(UUID chatId, int page, int size) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);

        if (chatEntity == null) {
            throw new EntityNotFoundException("Chat with id " + chatId + " not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<MessageEntity> messagesPage = chatRepository.findAllByIdOrderByUpdatedAt(chatId, pageable);

        return messagesPage.map(
                message -> new MessageGetResponseDto(
                        message.getId(),
                        message.getChat().getId(),
                        message.getContent()
                )
        );
    }

}
