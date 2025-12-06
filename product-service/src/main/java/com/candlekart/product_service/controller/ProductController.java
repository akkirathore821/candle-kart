package com.candlekart.product_service.controller;


import com.candlekart.product_service.dto.ProductRequest;
import com.candlekart.product_service.dto.ProductResponse;
import com.candlekart.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/id/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySKU(@PathVariable String sku) {
        return ResponseEntity.ok(productService.getProductBySKU(sku));
    }

}



//        Todo
//        | Endpoint                            | Method | Description                                               |
//        | ----------------------------------- | ------ | --------------------------------------------------------- |
//        | `/api/products`                     | `GET`  | Get all active products (paginated, searchable, sortable) |
//        | `/api/products/{id}`                | `GET`  | Get product details by ID                                 |
//        | `/api/products/category/{category}` | `GET`  | Get products by category                                  |
//        | `/api/products/search`              | `GET`  | Search by name, keyword, or description                   |
//        | `/api/products/sku/{sku}`           | `GET`  | Get product by SKU (unique identifier)                    |
