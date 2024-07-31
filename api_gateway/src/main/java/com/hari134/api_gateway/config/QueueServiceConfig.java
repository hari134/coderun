package com.hari134.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.hari134.api_gateway.service.QueueService;

@Configuration
public class QueueServiceConfig {

    @Autowired
    @Qualifier("rabbitMQService")
    private QueueService rabbitMQService;

    @Bean
    @Primary
    public QueueService queueService(@Value("${queue.type:rabbitmq}") String queueType) {
        switch (queueType.toLowerCase()) {
            case "rabbitmq":
                return rabbitMQService;
            default:
                throw new IllegalArgumentException("Unknown queue type: " + queueType);
        }
    }
}
