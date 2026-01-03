package com.candlekart.elasticsearch_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class ProductDocument {

    @Id
    private String id;

    private UUID productId;
    private String sku;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String currency;
    private String imageUrl;

    private Boolean inStock;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
//Todo Edit the Product Entity according to below sample
//{
//        "productId": "uuid",
//        "sku": "SKU12345",
//        "name": "Wireless Headphones",
//        "description": "...",
//        "category": "Audio",
//        "price": 2499.00,
//        "currency": "INR",
//        "attributes": {"color": "black", "battery": "24h"},
//        "images": ["url1","url2"],
//        "createdAt": "2025-10-29T..."
//        "updatedAt": "2025-10-29T..."
//}

//private String id; // use sku or sku:productId
//private String productId;
//private String sku;
//private String name;
//private String description;
//private List<String> category;
//private Double price;
//private String currency;
//private Map<String, String> attributes;
//private List<String> images;
//private Boolean inStock;
//private Instant updatedAt;

