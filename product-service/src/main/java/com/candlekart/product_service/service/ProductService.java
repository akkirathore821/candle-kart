package com.candlekart.product_service.service;

import com.candlekart.product_service.dto.ProductRequest;
import com.candlekart.product_service.dto.ProductResponse;
import com.candlekart.product_service.exc.NotFoundException;
import com.candlekart.product_service.model.Product;
import com.candlekart.product_service.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    private ProductResponse toDto(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .imageUrl(product.getImageUrl())
                .build();
    }
    private Product toEntity(ProductRequest request){
        return Product.builder()
                .sku(request.getSku())
                .productId(request.getProductId())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .imageUrl(request.getImageUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ProductResponse createProduct(ProductRequest request) {
        return toDto(productRepository.save(toEntity(request)));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream().map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(ProductRequest request) {
        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getDescription() != null)
            product.setDescription(request.getDescription());
        if (request.getCategory() != null)
            product.setCategory(request.getCategory());
        if (request.getPrice() != null)
            product.setPrice(request.getPrice());
        if (request.getCurrency() != null)
            product.setCurrency(request.getCurrency());
        if (request.getStock() != null)
            product.setStock(request.getStock());
        if (request.getImageUrl() != null)
            product.setImageUrl(request.getImageUrl());

        product.setUpdatedAt(LocalDateTime.now());

        return toDto(productRepository.save(product));
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
