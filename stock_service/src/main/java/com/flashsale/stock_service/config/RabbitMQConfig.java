package com.flashsale.stock_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {
    public static final String STOCK_QUEUE = "stock-update-queue";

    @Bean
    public Queue stockQueue() {
        return new Queue(STOCK_QUEUE, true);
    }
}
