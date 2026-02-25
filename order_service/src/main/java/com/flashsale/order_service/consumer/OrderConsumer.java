package com.flashsale.order_service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.flashsale.order_service.repository.OrderRepository;
import com.flashsale.order_service.event.OrderEvent;
import com.flashsale.order_service.entity.Order;
import com.flashsale.order_service.entity.OrderStatus;

@Component
public class OrderConsumer {
    private final OrderRepository orderRepository;

    public OrderConsumer(OrderRepository orderRepository) {
           this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = "orderQueue")
    public void handleOrder(OrderEvent event) {
           Order order = new Order();
           order.setUserId(event.getUserId());
           order.setProductId(event.getProductId());
           order.setOrderStatus(OrderStatus.SUCCESS);
           order.setQuantity(event.getQuantity());

           orderRepository.save(order);

           System.out.println("[SAMBIT] -- Order saved asynchonously " + order.getId());
    }
}
