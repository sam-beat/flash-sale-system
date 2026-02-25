package com.flashsale.stock_service.event;

public class StockReducedEvent {

    private Long productId;
    private Integer quantity;

    public StockReducedEvent() {}

    public StockReducedEvent(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
}
