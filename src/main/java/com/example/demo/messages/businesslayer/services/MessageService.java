package com.example.demo.messages.businesslayer.services;

import com.example.demo.amq.dtos.prompt.PromptMessage;
import com.example.demo.amq.services.PromptProducer;
import com.example.demo.messages.businesslayer.dtos.CreateMessageResponseDto;
import com.example.demo.messages.businesslayer.dtos.Message;
import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.infrastructure.errors.EntityNotFoundException;
import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.datalayer.entities.MessageEntity;
import com.example.demo.messages.datalayer.enums.SenderType;
import com.example.demo.messages.datalayer.repositories.MessageForAgent;
import com.example.demo.messages.datalayer.repositories.MessagesRepository;
import com.example.demo.chats.datalayer.repositories.ChatRepository;
import com.example.demo.globals.PageResponseDto;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final ChatRepository chatRepository;
    private final PromptProducer  promptProducer;

    @Transactional
    public CreateMessageResponseDto createMessageFromClient(UUID chatId, String content) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);
        System.out.println(content);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(content);
        messageEntity.setChat(chatEntity);
        messageEntity.setRole(SenderType.user);
        messageEntity.setToolCalls(null);
        messageEntity.setToolCallId(null);

        MessageEntity saved = messagesRepository.save(messageEntity);

        PromptMessage promptMessage = getChatHistory(saved.getChat().getId());

        promptProducer.sendPrompt(promptMessage);

        return new CreateMessageResponseDto(
                saved.getId(),
                saved.getChat().getId(),
                saved.getContent(),
                saved.getRole()
        );
    };

    @Transactional
    public MessageEntity createMessageFromAgent(UUID chatId, Message message) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(message.content());
        messageEntity.setChat(chatEntity);
        messageEntity.setRole(message.role());
        messageEntity.setToolCallId(message.toolCallId());
        messageEntity.setToolCalls(message.toolCalls());
        return messagesRepository.save(messageEntity);
    }


    public PageResponseDto<MessageGetResponseDto> getMessagesByChatId(UUID chatId, int page, int size) {
        // ... проверка chatEntity ...

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Specification<MessageEntity> spec = Specification.where((root, query, cb) ->
                cb.equal(root.get("chat").get("id"), chatId));

        // Добавляем условие WHERE role IN ('user', 'assistant')
        spec = spec.and((root, query, cb) ->
                root.get("role").in(SenderType.user, SenderType.assistant));

        spec = spec.and((root, query, cb) ->
                cb.isNotNull(root.get("content")));

        spec = spec.and((root, query, cb) ->
                       cb.notEqual(root.get("content"), ""));

        Page<MessageEntity> messagesPage = messagesRepository.findAll(spec, pageable);

        Page<MessageGetResponseDto> dtoPage = messagesPage.map(
                message -> new MessageGetResponseDto(
                        message.getId(),
                        message.getChat().getId(),
                        message.getContent(),
                        message.getRole()
                )
        );

        return new PageResponseDto<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );

    }

    public PromptMessage getChatHistory(UUID chatId) {
        ChatEntity chatEntity = chatRepository.getChatById(chatId);
        if (chatEntity == null) {
            throw new EntityNotFoundException("Chat with id " + chatId + " not found");
        }

        List<MessageForAgent> messageEntities = messagesRepository.findByChat_IdOrderByCreatedAtAsc(chatId);

        return new PromptMessage(chatId,
                messageEntities.stream()
                        .map(messageForAgent -> new Message(
                                messageForAgent.getRole(),         // SenderType: USER или AGENT
                                messageForAgent.getContent(),      // текст сообщения
                                messageForAgent.getToolCalls(),    // List<ToolCallDto>, если есть
                                messageForAgent.getToolCallId()   // String, если есть
                        ))
                        .toList()
        );

    }

}
