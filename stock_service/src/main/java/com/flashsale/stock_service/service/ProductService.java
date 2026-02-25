package com.flashsale.stock_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.flashsale.stock_service.repository.ProductRepository;
import com.flashsale.stock_service.entity.Product;

@Service
public class ProductService {
    private final ProductRepository repository;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public ProductService(ProductRepository repository, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public void loadStockToRedis(Long productId) {
        Product product = repository.findById(productId)
                                    .orElseThrow( () -> new RuntimeException("Product not found"));

        redisTemplate.opsForValue().set("stock:" + productId, product.getStock().toString());
    }

    @Transactional
    public void reduceStock(Long productId, Integer quantity) {

        String key = "stock:" + productId;

        Long remaining = redisTemplate.opsForValue().decrement(key, quantity);

        if(remaining == null){
            throw new RuntimeException("PRODUCT_NOT_FOUND");
        }

        if(remaining < 0) {
            //revert the decrement bcz we don't have enough product to ship the order
            redisTemplate.opsForValue().increment(key, quantity);

            throw new RuntimeException("OUT_OF_STOCK");
        }
       
    }
    
}
