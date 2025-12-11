package com.candlekart.cart_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private String sku;
    private Integer quantity;
    private BigDecimal price;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "OrderItemResponse{" +
                "sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
