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
@RequestMapping("/api/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("create")
    public ResponseEntity<List<ProductResponse>> createProduct(@RequestBody List<ProductRequest> request) throws Exception {
        return ResponseEntity.ok(productService.createProduct(request));
    }
    @PutMapping("/update")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(request));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}



//        Todo
//        | Endpoint                     | Method   | Description                                 |
//        | ---------------------------- | -------- | ------------------------------------------- |
//        | `/api/products`              | `POST`   | Create a new product                        |
//        | `/api/products/{id}`         | `PUT`    | Full update product details                 |
//        | `/api/products/{id}`         | `PATCH`  | Partial update (only some fields)           |
//        | `/api/products/{id}`         | `DELETE` | Delete product                              |
//        | `/api/products/bulk`         | `POST`   | Create multiple products in bulk            |
//        | `/api/products/{id}/status`  | `PATCH`  | Activate/deactivate a product               |
//        | `/api/products/{id}/stock`   | `PATCH`  | Update stock quantity only                  |
//        | `/api/products/price/update` | `PATCH`  | Bulk update prices by category or condition |

