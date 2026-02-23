package com.flashsale.user_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;             

    @Column(nullable = false)
    private BigDecimal balance;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public User() {

    }

    public Long getId() {
           return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public BigDecimal getBalance() {
           return this.balance;
    }

    public LocalDateTime getCreationTime() {
        return this.createdAt;
    }

    public void setUsername(String UserName) {
        this.username = UserName;
    }

    public void setBalance(BigDecimal Balance) {
        this.balance = Balance;
    }

}
