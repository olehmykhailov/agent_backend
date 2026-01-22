package com.example.demo.amq.services;

import com.example.demo.amq.dtos.prompt.PromptMessage;
import com.example.demo.infrastructure.configs.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPrompt(PromptMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.CHAT_EXCHANGE,
                "prompt",
                message
        );
    }
}
