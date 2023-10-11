package com.example.testfinal.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public Queue importQueue(){
        return new Queue("import-queue", false);
    }
}
