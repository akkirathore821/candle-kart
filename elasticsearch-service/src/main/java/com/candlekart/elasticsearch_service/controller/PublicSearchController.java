package com.candlekart.elasticsearch_service.controller;

import com.candlekart.elasticsearch_service.model.ProductDocument;

import com.candlekart.elasticsearch_service.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/search")
@RequiredArgsConstructor
public class PublicSearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDocument>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false,defaultValue = "price_asc") String sort
    ) {
        return ResponseEntity.ok(elasticSearchService.search(q, category, minPrice, maxPrice, null, page, size, sort));
    }

//    @GetMapping("/suggest")
//    public ResponseEntity<List<ProductSuggestion>> suggest(@RequestParam String q,
//                                                           @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(searchService.suggest(q, size));
//    }
}
