package com.hari134.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.hari134.api_gateway.service.queue.QueueService;

@Configuration
public class QueueServiceConfig {

    @Autowired
    private QueueService rabbitMQService;

    @Autowired
    private QueueService kafkaService;

    @Autowired
    private QueueService sqsService;

    @Bean
    @Primary
    public QueueService queueService(@Value("${queue.type:rabbitmq}") String queueType) {
        switch (queueType.toLowerCase()) {
            case "rabbitmq":
                return rabbitMQService;
            case "kafka":
                return kafkaService;
            case "sqs":
                return sqsService;
            default:
                throw new IllegalArgumentException("Unknown queue type: " + queueType);
        }
    }
}
