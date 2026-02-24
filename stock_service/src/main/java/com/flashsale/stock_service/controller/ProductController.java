package com.flashsale.stock_service.controller;

import com.flashsale.stock_service.entity.Product;
import com.flashsale.stock_service.repository.ProductRepository;
import com.flashsale.stock_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
       try {
        productService.reduceStock(id, quantity);
        Product updatedProduct = repository.findById(id)
                                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found"));

        return ResponseEntity.ok(updatedProduct);
       } catch (RuntimeException e) {
            
            switch(e.getMessage()) {
                case "OUT_OF_STOCK":
                    return ResponseEntity
                              .status(HttpStatus.BAD_REQUEST)
                              .body("Out of stock");

                case "HIGH_TRAFFIC": 
                    return ResponseEntity 
                              .status(HttpStatus.CONFLICT)
                              .body("High Traffic. Please Retry");

                case "PRODUCT_NOT_FOUND":
                    return ResponseEntity
                              .status(HttpStatus.NOT_FOUND)
                              .body("Product Not Found.");

                default:
                    return ResponseEntity
                              .status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("Unexpected Error");
            }
       }
    }

}
