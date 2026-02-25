package com.flashsale.order_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
        try {
            restTemplate.postForObject("http://localhost:8082/products/" + productId + 
            "/reduce?quantity=" + quantity, 
             null, 
             Void.class);
        } catch (HttpClientErrorException e) {
            
            if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException("OUT_OF_STOCK");
            }

            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new RuntimeException("HIGH_TRAFFIC");
            }

            throw new RuntimeException("STOCK_SERVICE_ERROR");
        }

        //Order entry is created only when a successful order gets executed
        Order order = new Order();
        order.setUserId(userId);
        order.setQuantity(quantity);
        order.setProductId(productId);
        order.setOrderStatus(OrderStatus.SUCCESS);

        return orderRepository.save(order);
    }
}
