package com.flashsale.stock_service.controller;

import com.flashsale.stock_service.entity.Product;
import com.flashsale.stock_service.repository.ProductRepository;
import com.flashsale.stock_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository repository;
    private final ProductService productService;

    public ProductController(ProductRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestParam String name, @RequestParam BigDecimal price, @RequestParam Integer stock) {

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);

        return repository.save(product);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return repository.findById(id)
                         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
    }

     @PostMapping("/{id}/reduce")
    public Product reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        productService.reduceStock(id, quantity);

        return repository.findById(id)
                         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));
    }

}
