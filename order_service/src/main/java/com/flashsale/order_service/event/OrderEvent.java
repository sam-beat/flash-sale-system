package com.flashsale.order_service.event;

import java.io.Serializable;

public class OrderEvent implements Serializable {

    private Long userId;
    private Long productId;
    private Integer quantity;

    public OrderEvent() {}

    public OrderEvent(Long userId, Long productId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getUserId() { return userId; }
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
}
