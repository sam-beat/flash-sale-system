package com.flashsale.stock_service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.flashsale.stock_service.repository.ProductRepository;

import jakarta.transaction.Transactional;

import com.flashsale.stock_service.config.RabbitMQConfig;
import com.flashsale.stock_service.entity.Product;
import com.flashsale.stock_service.event.StockReducedEvent;

@Component
public class StockUpdateConsumer {
    private final ProductRepository repository;

    public StockUpdateConsumer(ProductRepository repository) {
            this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
    @Transactional
    public void handleStockUpdate(StockReducedEvent event) {

        Product product = repository.findById(event.getProductId())
                                    .orElseThrow();

        product.setStock(product.getStock() - event.getQuantity());

        repository.save(product);
    }
}
