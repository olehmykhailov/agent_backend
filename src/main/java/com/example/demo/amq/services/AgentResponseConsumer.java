package com.example.demo.amq.services;



import com.example.demo.amq.dtos.response.ResponseMessage;
import com.example.demo.amq.dtos.response.toolcall.ToolCallDto;

import com.example.demo.infrastructure.configs.RabbitConfig;


import com.example.demo.messages.businesslayer.dtos.Message;
import com.example.demo.messages.businesslayer.dtos.MessageGetResponseDto;
import com.example.demo.messages.businesslayer.services.MessageService;

import com.example.demo.messages.datalayer.entities.MessageEntity;


import com.example.demo.messages.datalayer.enums.SenderType;
import com.example.demo.vacancies.businesslayer.dtos.VacancyGetResponseDto;
import com.example.demo.vacancies.businesslayer.services.VacanciesService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AgentResponseConsumer {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final VacanciesService vacanciesService;
    private final ObjectMapper objectMapper;
    private final PromptProducer promptProducer;


    @RabbitListener(queues = RabbitConfig.RESPONSE_QUEUE)
    public void handleResponse(ResponseMessage msg) throws JsonProcessingException {
        System.out.println(msg);
        if (msg.message() == null) {
            return;
        }

        MessageEntity saved = messageService.createMessageFromAgent(msg.chatId(), msg.message());

        if (msg.toolFilters() == null && msg.message().toolCalls() == null) {
            messagingTemplate.convertAndSend(
                    "/topic/chat/" + msg.chatId(),
                    new MessageGetResponseDto(
                            saved.getId(),
                            saved.getChat().getId(),
                            saved.getContent(),
                            saved.getRole()
                    )
            );

        } else {
            String content = null;
            for (ToolCallDto toolCall : msg.message().toolCalls()) {
                if (toolCall.function().name().equals("build_job_filters")) {
                    List<VacancyGetResponseDto> vacancies = vacanciesService.executeAgentCall(msg.chatId(), msg.toolFilters());
                    content = objectMapper.writeValueAsString(vacancies);
                }
                messageService.createMessageFromAgent(msg.chatId(), new Message(
                        SenderType.tool,
                        content,
                        null,
                        toolCall.id()
                ));
            }
            promptProducer.sendPrompt(messageService.getChatHistory(msg.chatId()));
        }
    }
}
