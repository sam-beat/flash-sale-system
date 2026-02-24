package com.flashsale.stock_service.service;

import org.springframework.http.HttpStatus;
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
        Product product = repository.findById(productId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        if(product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock!!!");
        }

        product.setStock(product.getStock() - quantity);

        repository.save(product);
    }
    
}
