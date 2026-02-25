package com.flashsale.order_service.controller;

import com.flashsale.order_service.entity.Order;
import com.flashsale.order_service.entity.OrderStatus;
import com.flashsale.order_service.repository.OrderRepository;
import com.flashsale.order_service.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository repository;
    private final OrderService orderService; 

    public OrderController (OrderRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity) {
        try {   
            Order order = orderService.createOrder(userId, productId, quantity);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
               
            switch (e.getMessage()) {
                case "OUT_OF_STOCK":
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Out of stock");
                case "HIGH_TRAFFIC":
                    return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("High traffic. Please retry.");
                default:
                    return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected error");
            }
        }
    }

@GetMapping("/{id}")
public Order getOrder(@PathVariable Long id) {
    return repository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Order not found"
                    ));
}

}
