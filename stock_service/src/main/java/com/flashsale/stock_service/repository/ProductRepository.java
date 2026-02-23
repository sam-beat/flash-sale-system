package com.flashsale.stock_service.repository;

import com.flashsale.stock_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}