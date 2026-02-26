package com.flashsale.stock_service.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.flashsale.stock_service.repository.ProductRepository;

@Configuration
public class RedisWarmupConfig {
    @Bean
    public ApplicationRunner loadRedisOnStartUp(ProductRepository repository, StringRedisTemplate redisTemplate) {

        return args -> {
            repository.findAll().forEach(product-> {
                String key = "product:" + product.getId();
                redisTemplate.opsForValue()
                             .set(key, product.getStock().toString());

            });
        };
    }
}
