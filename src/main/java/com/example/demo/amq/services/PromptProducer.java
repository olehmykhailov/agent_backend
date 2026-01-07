package com.example.demo.amq.services;

import com.example.demo.amq.dtos.PromptMessage;
import com.example.demo.configs.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPrompt(PromptMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                "prompt",
                message
        );
    }
}
