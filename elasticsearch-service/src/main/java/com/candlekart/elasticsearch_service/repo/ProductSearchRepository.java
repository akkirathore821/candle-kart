package com.candlekart.elasticsearch_service.repo;

import com.candlekart.elasticsearch_service.model.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    Optional<ProductDocument> findBySku(String sku);
    // custom search via native queries in service
}

