package com.hari134.coderun.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Value("${spring.rabbitmq.url}")
  private String rabbitmqUrl;

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setUri(rabbitmqUrl);
    return connectionFactory;
  }

  @Bean
  public Queue userRequestQueue() {
    return new Queue("request-queue", true); // Declare queue with automatic creation
  }

  @Bean
  public Queue userResponseQueue() {
    return new Queue("response-queue", true); // Declare queue with automatic creation
  }
}
