package com.hari134.coderun.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userRequestQueue() {
        return new Queue("request-queue", true); // Declare queue with automatic creation
    }

    @Bean
    public Queue userResponseQueue() {
        return new Queue("response-queue", true); // Declare queue with automatic creation
    }
}

