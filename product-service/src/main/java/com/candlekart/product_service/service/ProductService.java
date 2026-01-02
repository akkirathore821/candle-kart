package com.candlekart.product_service.service;

import com.candlekart.product_service.dto.*;
import com.candlekart.product_service.exc.BadRequestException;
import com.candlekart.product_service.exc.NotFoundException;
import com.candlekart.product_service.exc.ValidationException;
import com.candlekart.product_service.kafka.KafkaProducer;
import com.candlekart.product_service.model.Product;
import com.candlekart.product_service.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.candlekart.product_service.constants.Constants.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Transactional
    public List<ProductResponse> createProduct(List<ProductRequest> request) throws Exception {

//        todo
//        Transactional + Kafka publish = Problem
//        You are in a single @Transactional method.
//        Meaning:
//            DB save is done inside the transaction
//            Kafka publish is done inside the same transaction
//        ⚠️ If Kafka publish fails → DB insert is already committed
//        ⚠️ If DB insert fails (constraint), Kafka may already have published message
//        👉 This is a classic "dual-write" consistency problem".
//        Correct solution:
//            Use Transactional Outbox Pattern or Spring @TransactionalEventListener.

        List<Product> products = new ArrayList<>();

        try{
            for (ProductRequest pr : request)
                if (!productRepository.existsBySku(pr.getSku()))
                    products.add(toEntity(pr));

            products = productRepository.saveAll(products);

            List<ProductResponse> responses = products.stream().map(this::toDto).toList();

            ElasticSearchMessageDTO elasticSearchMessageDTO = new ElasticSearchMessageDTO("create", LocalDateTime.now(), responses.size(), responses);
            InventoryRequestList inventoryRequestList = InventoryRequestList.builder()
                    .itemList(products.stream()
                            .map(product -> InventoryRequest.builder()
                                    .sku(product.getSku())
                                    .stock(0).build())
                            .toList())
                    .build();



            //Publishing the message to the Inventory and Elasticsearch
            if(inventoryRequestList != null && !inventoryRequestList.getItemList().isEmpty())
                publish(Create_Product_In_Inventory_Topic_Name, inventoryRequestList);
            if(!elasticSearchMessageDTO.getProducts().isEmpty())
                publish(Create_Product_In_ElasticSearch_Topic_Name, elasticSearchMessageDTO);

            return responses;

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }




//        ToDo
//        If SKU exists → it skips silently
//        The client won’t know which ones were skipped
//        Can cause unexpected partial imports
//        Better to return a report of skipped SKUs.
    }

    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return  toDto(product);
    }
    public ProductResponse getProductBySKU(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new ValidationException("SKU cannot be empty");
        }

        Product product = productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new NotFoundException("Product not found with SKU: " + sku));

        return toDto(product);
    }
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> res = productRepository.findAll().stream()
                .map(this::toDto)
                .toList();

        if(res.isEmpty())   throw new NotFoundException("Product not found");
        return res;

    }
    @Transactional
    public ProductResponse updateProduct(ProductRequest request) {

        //Todo Need to call Elasticsearch service to update product

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        boolean updated = false;

        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
            updated = true;
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            product.setDescription(request.getDescription());
            updated = true;
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
            updated = true;
        }
        if (request.getPrice() != null && request.getPrice() >= 0) {
            product.setPrice(request.getPrice());
            updated = true;
        }
        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            product.setCurrency(request.getCurrency());
            updated = true;
        }
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            product.setImageUrl(request.getImageUrl());
            updated = true;
        }
        if (!updated) {
            throw new BadRequestException("No fields provided for update");
        }
        product.setUpdatedAt(LocalDateTime.now());

        List<ProductResponse> products = new ArrayList<>();
        products.add(toDto(product));
        ElasticSearchMessageDTO elasticSearchMessageDTO = new ElasticSearchMessageDTO("update", LocalDateTime.now(), products.size(), products);
        publish(Update_Product_In_ElasticSearch_Topic_Name, elasticSearchMessageDTO);


        return toDto(product);
    }
    public void deleteProduct(UUID productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.deleteById(productId);
        //todo delete the products from the elasticsearch and inventory
    }
















    private void publish(String topic, Object evt) {
        kafkaProducer.publish(topic, evt);
    }
    private ProductResponse toDto(Product product) {
        return ProductResponse.builder()
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
}
