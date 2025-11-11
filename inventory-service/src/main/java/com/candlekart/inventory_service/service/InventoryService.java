package com.candlekart.inventory_service.service;

import com.candlekart.inventory_service.dto.InventoryRequest;
import com.candlekart.inventory_service.dto.InventoryResponse;
import com.candlekart.inventory_service.exc.InsufficientStockException;
import com.candlekart.inventory_service.exc.NotFoundException;
import com.candlekart.inventory_service.model.Inventory;
import com.candlekart.inventory_service.repo.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;
    private Inventory toEntity(InventoryRequest request) {
        return Inventory.builder()
                .productId(request.getProductId())
                .skuCode(request.getSkuCode())
                .quantity(request.getQuantity())
                .build();
    }
    private InventoryResponse toDto(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .inStock(inventory.getQuantity() > 0)
                .quantity(inventory.getQuantity())
                .build();
    }

    public InventoryResponse addInventory(InventoryRequest request) {
        return toDto(inventoryRepository.save(toEntity(request)));
    }
    public List<InventoryResponse> addAllInventory(List<InventoryRequest> requests) {
        List<Inventory> inventories = requests
                .stream()
                .map(this::toEntity)
                .toList();
        return inventoryRepository.saveAll(inventories)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public InventoryResponse getInventoryBySku(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new NotFoundException("SKU not found: " + skuCode));
        return toDto(inventory);
    }

    @Transactional
    public void reduceStock(Map<String, Integer> skuQuantityMap) {
        for (Map.Entry<String, Integer> entry : skuQuantityMap.entrySet()) {
            Inventory inventory = inventoryRepository.findBySkuCode(entry.getKey())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getKey()));

            if (inventory.getQuantity() < entry.getValue()) {
                throw new InsufficientStockException("Insufficient stock for SKU: " + entry.getKey());
            }
            inventory.setQuantity(inventory.getQuantity() - entry.getValue());
        }
    }

    @Transactional
    public void restoreStock(Map<String, Integer> skuQuantityMap) {
        for (Map.Entry<String, Integer> entry : skuQuantityMap.entrySet()) {
            Inventory inventory = inventoryRepository.findBySkuCode(entry.getKey())
                    .orElseThrow(() -> new NotFoundException("SKU not found: " + entry.getKey()));
            inventory.setQuantity(inventory.getQuantity() + entry.getValue());
        }
    }

    public List<InventoryResponse> checkStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes)
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


}

