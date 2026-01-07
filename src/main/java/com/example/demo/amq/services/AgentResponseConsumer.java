package com.example.demo.amq.services;

import com.example.demo.amq.dtos.ResponseMessage;
import com.example.demo.configs.RabbitConfig;
import com.example.demo.responses.businesslayer.dtos.CreateResponseRequestDto;
import com.example.demo.responses.businesslayer.dtos.CreateResponseResponseDto;
import com.example.demo.responses.businesslayer.services.ResponseService;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentResponseConsumer {

    private final ResponseService responseService;
    private final SimpMessagingTemplate messagingTemplate;


    @RabbitListener(queues = RabbitConfig.RESPONSE_QUEUE)
    public void handleResponse(ResponseMessage msg) {

        // 1️⃣ сохраняем ответ
        CreateResponseResponseDto saved =
                responseService.createResponse(
                        new CreateResponseRequestDto(
                                msg.chatId(),
                                msg.messageId(),
                                msg.content()
                        )
                );

        // 2️⃣ отправляем клиентам
        messagingTemplate.convertAndSend(
                "/topic/chat/" + msg.chatId(),
                saved
        );
    }
}
