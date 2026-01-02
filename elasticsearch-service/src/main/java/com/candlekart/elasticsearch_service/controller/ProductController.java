package com.candlekart.elasticsearch_service.controller;

import com.candlekart.elasticsearch_service.dto.ProductRRequest;
import com.candlekart.elasticsearch_service.model.ProductDocument;

import com.candlekart.elasticsearch_service.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elastic_product")
public class ProductController {

    @Autowired
    private ElasticSearchService elasticSearchService;

//    @PostMapping("/addProducts")
//    public ResponseEntity<ProductDocument> addProducts(@RequestBody ProductRRequest request) {
//        return ResponseEntity.ok(elasticSearchService.addProduct(request));
//    }

//    @PostMapping("/addAllProducts")
//    public ResponseEntity<List<ProductDocument>> addAllProducts(@RequestBody List<ProductRRequest> requests) {
//        return ResponseEntity.ok(elasticSearchService.addAllProducts(requests));
//    }

//    @GetMapping("/suggest")
//    public ResponseEntity<List<ProductSuggestion>> suggest(@RequestParam String q,
//                                                           @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(searchService.suggest(q, size));
//    }
}
