package com.candlekart.elasticsearch_service.repo;

import com.candlekart.elasticsearch_service.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.UUID;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findBySku(String sku);
    // custom search via native queries in service
}

