package com.flashsale.order_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flashsale.order_service.repository.OrderRepository;
import com.flashsale.order_service.entity.Order;
import com.flashsale.order_service.entity.OrderStatus;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public Order createOrder(Long userId, Long productId, Integer quantity) {
        
        restTemplate.postForObject("http://localhost:8082/products/" + productId + 
        "/reduce?quantity=" + quantity, 
        null, 
        Void.class);

        Order order = new Order();
        order.setUserId(userId);
        order.setQuantity(quantity);
        order.setProductId(productId);
        order.setOrderStatus(OrderStatus.SUCCESS);

        return orderRepository.save(order);
    }
}
