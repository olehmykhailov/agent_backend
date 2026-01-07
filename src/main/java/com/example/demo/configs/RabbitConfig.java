package com.example.demo.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String PROMPT_QUEUE = "prompt.queue";
    public static final String RESPONSE_QUEUE = "response.queue";
    public static final String EXCHANGE = "chat.exchange";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue promptQueue() {
        return new Queue(PROMPT_QUEUE);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE);
    }

    @Bean
    public Binding promptBinding() {
        return BindingBuilder.bind(promptQueue()).to(directExchange()).with("prompt");
    }

    @Bean
    public Binding responseBinding() {
        return BindingBuilder.bind(responseQueue()).to(directExchange()).with("response");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
