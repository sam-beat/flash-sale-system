package com.flashsale.stock_service.service;

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

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void reduceStock(Long productId, Integer quantity) {

        int maxRetries = 3;
        int attempt = 0;

        while(attempt < maxRetries) {
            try {
                Product product = repository.findById(productId)
                                   .orElseThrow(() -> new RuntimeException("Product not found"));
                
                if(product.getStock() < quantity) {
                    throw new RuntimeException("Out of Stock.");
                }

                product.setStock(product.getStock() - quantity);
                repository.save(product);

                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;
                System.out.println("Retrying due to version conflict... Attempt: "+ attempt);

                if(attempt >= maxRetries) {
                    throw new RuntimeException("High Traffic. Please Retry.");
                }
            }
        }
       
    }
    
}
