package com.flashsale.order_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.flashsale.order_service.repository.OrderRepository;
import com.flashsale.order_service.entity.Order;
import com.flashsale.order_service.entity.OrderStatus;
import com.flashsale.order_service.event.OrderEvent;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String createOrder(Long userId, Long productId, Integer quantity) {

        // 1. Check Stock (Redis already handles concurrency)
            restTemplate.postForObject("http://localhost:8082/products/" + productId + 
            "/reduce?quantity=" + quantity, 
             null, 
             Void.class);

        // 2. Publish Event
        OrderEvent event = new OrderEvent(userId, productId, quantity);
        rabbitTemplate.convertAndSend("orderQueue", event);

        return "Order is being processed ";

    }
}
