package com.candlekart.elasticsearch_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long productId;
    private String sku;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String currency;
    private String imageUrl;
    private Boolean inStock;
}
