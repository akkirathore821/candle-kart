package com.candlekart.elasticsearch_service.controller;

import com.candlekart.elasticsearch_service.dto.ProductRequest;
import com.candlekart.elasticsearch_service.model.ProductDocument;

import com.candlekart.elasticsearch_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elastic_product")
public class ProductController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/addProducts")
    public ResponseEntity<ProductDocument> addProducts(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(searchService.addProduct(request));
    }

    @PostMapping("/addAllProducts")
    public ResponseEntity<List<ProductDocument>> addAllProducts(@RequestBody List<ProductRequest> requests) {
        return ResponseEntity.ok(searchService.addAllProducts(requests));
    }

//    @GetMapping("/suggest")
//    public ResponseEntity<List<ProductSuggestion>> suggest(@RequestParam String q,
//                                                           @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(searchService.suggest(q, size));
//    }
}
