package com.candlekart.inventory_service.service;

import com.candlekart.inventory_service.dto.InventoryRequest;
import com.candlekart.inventory_service.dto.InventoryRequestList;
import com.candlekart.inventory_service.dto.InventoryResponse;
import com.candlekart.inventory_service.dto.OrderResponse;
import com.candlekart.inventory_service.exc.InsufficientStockException;
import com.candlekart.inventory_service.exc.NotFoundException;
import com.candlekart.inventory_service.model.InventoryProduct;
import com.candlekart.inventory_service.repo.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public void onOrderCreation(OrderResponse order){
        //todo


    }

    public InventoryResponse addNewProduct(InventoryRequest request) {
        return toDto(inventoryRepository.save(toEntity(request)));
    }

    public List<InventoryResponse> addAllInventory(List<InventoryRequest> requests) {
        List<InventoryProduct> inventories = requests
                .stream()
                .map(this::toEntity)
                .toList();
        return inventoryRepository.saveAll(inventories)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryBySku(String skuCode) {
        InventoryProduct inventory = inventoryRepository.findBySku(skuCode)
                .orElseThrow(() -> new NotFoundException("Product not found: " + skuCode));
        return toDto(inventory);
    }

    @Transactional
    public void reduceStock(List<InventoryRequest> requests) {
        for (InventoryRequest entry : requests) {
            InventoryProduct currentInventory = inventoryRepository.findBySku(entry.getSku())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getSku()));

            if (currentInventory.getStock() < entry.getStock()) {
                throw new InsufficientStockException("Insufficient stock for SKU: " + entry.getSku());
            }
            currentInventory.setStock(currentInventory.getStock() - entry.getStock());
        }
    }

    @Transactional
    public void restoreStock(List<InventoryRequest> requests) {
        for (InventoryRequest entry : requests) {
            InventoryProduct currentInventory = inventoryRepository.findBySku(entry.getSku())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getSku()));

            currentInventory.setStock(currentInventory.getStock() + entry.getStock());
        }
    }

    @Transactional
    public void reserveStock(InventoryRequestList requests){
        for (InventoryRequest entry : requests.getOrderList()) {
            InventoryProduct currentInventory = inventoryRepository.findBySku(entry.getSku())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getSku()));

            if (currentInventory.getStock() < entry.getStock()) {
                throw new InsufficientStockException("Insufficient stock for SKU: " + entry.getSku());
            }
            currentInventory.setStock(currentInventory.getStock() - entry.getStock());
            currentInventory.setReserved(currentInventory.getReserved() + entry.getStock());
        }
    }

    @Transactional
    public void releaseStock(InventoryRequestList requests){
        for (InventoryRequest entry : requests.getOrderList()) {
            InventoryProduct currentInventory = inventoryRepository.findBySku(entry.getSku())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getSku()));

            if (currentInventory.getReserved() < entry.getStock()) {
                throw new InsufficientStockException("Insufficient stock for SKU: " + entry.getSku());
            }
            currentInventory.setStock(currentInventory.getReserved() - entry.getStock());
            currentInventory.setStock(currentInventory.getStock() + entry.getStock());
        }
    }

    public List<InventoryResponse> checkStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuIn(skuCodes)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }



    private InventoryProduct toEntity(InventoryRequest request) {
        return InventoryProduct.builder()
                .sku(request.getSku())
                .stock(request.getStock())
                .reserved(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    private InventoryResponse toDto(InventoryProduct inventory) {
        return InventoryResponse.builder()
                .sku(inventory.getSku())
                .inStock(inventory.getStock() > 0)
                .stock(inventory.getStock())
                .build();
    }


}

