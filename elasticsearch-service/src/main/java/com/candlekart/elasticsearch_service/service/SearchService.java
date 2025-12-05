package com.candlekart.elasticsearch_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.candlekart.elasticsearch_service.dto.ProductRequest;
import com.candlekart.elasticsearch_service.model.ProductDocument;
import com.candlekart.elasticsearch_service.repo.ProductSearchRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    private ProductSearchRepository repository;

    public Page<ProductDocument> search(
            String q,
            String category,
            Double minPrice,
            Double maxPrice,
            Boolean inStock,
            int page,
            int size,
            String sort
    ) {

        try {
            SearchResponse<ProductDocument> response = elasticsearchClient.search(s -> s
                            .index("products")
                            .from(page * size)
                            .size(size)
                            .sort(so -> {
                                if ("price_asc".equalsIgnoreCase(sort)) {
                                    return so.field(f -> f.field("price").order(SortOrder.Asc));
                                } else if ("price_desc".equalsIgnoreCase(sort)) {
                                    return so.field(f -> f.field("price").order(SortOrder.Desc));
                                }
                                return so; // no sorting
                            })
                            .query(qb -> qb.bool(b -> {

                                // FULL TEXT SEARCH
                                if (q != null && !q.isEmpty()) {
                                    b.must(m -> m.multiMatch(mm -> mm
                                            .query(q)
                                            .fields("name", "description", "attributes.*")
                                            .fuzziness("AUTO")
                                            .type(TextQueryType.BestFields)
                                            .minimumShouldMatch("70%")
                                    ));
                                }

                                // CATEGORY FILTER
                                if (category != null && !category.isEmpty()) {
                                    b.filter(f -> f.term(t -> t.field("category").value(category)));
                                }

                                // todo PRICE RANGE FILTER
//                                if (minPrice != null || maxPrice != null) {
//                                    b.filter(f -> f.range(r -> {
//                                        r.field("price");
//                                        if (minPrice != null) r.gte(JsonData.of(minPrice));
//                                        if (maxPrice != null) r.lte(JsonData.of(maxPrice));
//                                        return r;
//                                    }));
//                                }

                                // STOCK FILTER
                                if (inStock != null) {
                                    b.filter(f -> f.term(t -> t.field("inStock").value(inStock)));
                                }

                                return b;
                            })),
                    ProductDocument.class
            );

            // Convert ES Hits -> Page<ProductDocument>
            List<ProductDocument> docs = response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

            long total = response.hits().total() != null
                    ? response.hits().total().value()
                    : docs.size();

            return new PageImpl<>(docs, PageRequest.of(page, size), total);
        }
        catch (Exception e) {
            throw new RuntimeException("Search failed", e);
        }
    }

    public ProductDocument addProduct(ProductRequest request) {
        ProductDocument doc = toDoc(request);
        return repository.save(doc);
    }

    public @Nullable List<ProductDocument> addAllProducts(List<ProductRequest> requests) {
        List<ProductDocument> docs = requests.stream()
                .map(this::toDoc)
                .toList();
        return (List<ProductDocument>) repository.saveAll(docs);
    }

    private ProductDocument toDoc (ProductRequest request){
        return ProductDocument.builder()
                .productId(request.getProductId())
                .sku(request.getSku())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .currency(request.getCurrency())
                .imageUrl(request.getImageUrl())
                .inStock(request.getInStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
