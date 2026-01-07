package com.example.demo.responses.businesslayer.services;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.infrastructure.errors.EntityNotFoundException;
import com.example.demo.messages.datalayer.entities.MessageEntity;
import com.example.demo.responses.businesslayer.dtos.CreateResponseRequestDto;
import com.example.demo.responses.businesslayer.dtos.CreateResponseResponseDto;
import com.example.demo.responses.businesslayer.dtos.ResponseGetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.responses.datalayer.entities.ResponseEntity;
import com.example.demo.chats.datalayer.repositories.ChatRepository;
import com.example.demo.messages.datalayer.repositories.MessagesRepository;
import com.example.demo.responses.datalayer.repositories.ResponseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final MessagesRepository messagesRepository;
    private final ChatRepository chatRepository;

    public ResponseService(ResponseRepository responseRepository, ChatRepository chatRepository,  MessagesRepository messagesRepository) {
        this.responseRepository = responseRepository;
        this.chatRepository = chatRepository;
        this.messagesRepository = messagesRepository;
    }

    public Page<ResponseGetResponseDto> getResponsesByChatId(UUID chatId, int page, int size) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);

        if (chatEntity == null) {
            throw new EntityNotFoundException("Chat with id " + chatId + " not found");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<ResponseEntity> responsesPage =  responseRepository.findAllByChatIdOrderByUpdatedAtDesc(chatId, pageable);

        return responsesPage.map(
                response -> new ResponseGetResponseDto(
                        response.getId(),
                        response.getMessage().getId(),
                        response.getChat().getId()
                )
        );

    }

    @Transactional
    public CreateResponseResponseDto createResponse(CreateResponseRequestDto createResponseRequestDto) {
        ChatEntity chatEntity = chatRepository.getChatById(createResponseRequestDto.chatId());

        if (chatEntity == null) {
            throw new EntityNotFoundException("Chat with id " + createResponseRequestDto.chatId() + " not found");
        }

        MessageEntity  messageEntity = messagesRepository.getMessageById(createResponseRequestDto.messageId());

        if  (messageEntity == null) {
            throw new EntityNotFoundException("Message with id " + createResponseRequestDto.messageId() + " not found");
        }

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setMessage(messageEntity);
        responseEntity.setChat(chatEntity);
        responseEntity.setContent(createResponseRequestDto.content());

        ResponseEntity createdResponse = responseRepository.save(responseEntity);

        return new CreateResponseResponseDto(
                createdResponse.getId(),
                createdResponse.getChat().getId(),
                createdResponse.getMessage().getId(),
                createdResponse.getContent()
        );





    }
}
