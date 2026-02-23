package com.flashsale.order_service.controller;

import com.flashsale.order_service.entity.Order;
import com.flashsale.order_service.entity.OrderStatus;
import com.flashsale.order_service.repository.OrderRepository;

import org.springframework.http.HttpStatus;
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

    public OrderController (OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Order createOrder(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity) {

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setOrderStatus(OrderStatus.PENDING);

        return repository.save(order);
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
