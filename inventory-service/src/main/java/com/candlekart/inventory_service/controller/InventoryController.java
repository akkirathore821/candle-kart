package com.candlekart.inventory_service.controller;

import com.candlekart.inventory_service.dto.InventoryRequest;
import com.candlekart.inventory_service.dto.InventoryResponse;
import com.candlekart.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addInventory(@RequestBody InventoryRequest request) {
        return ResponseEntity.ok(inventoryService.addInventory(request));
    }

    @PostMapping("/addAll")
    public ResponseEntity<List<InventoryResponse>> addAllInventory(@RequestBody List<InventoryRequest> requests) {
        return ResponseEntity.ok(inventoryService.addAllInventory(requests));
    }

    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryResponse> getInventoryBySku(@PathVariable String skuCode) {
        return ResponseEntity.ok(inventoryService.getInventoryBySku(skuCode));
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PostMapping("/check")
    public ResponseEntity<List<InventoryResponse>> checkStock(@RequestBody List<String> skuCodes) {
        return ResponseEntity.ok(inventoryService.checkStock(skuCodes));
    }

    @PostMapping("/reduce")
    public ResponseEntity<Void> reduceStock(@RequestBody Map<String, Integer> skuQuantityMap) {
        inventoryService.reduceStock(skuQuantityMap);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restore")
    public ResponseEntity<Void> restoreStock(@RequestBody Map<String, Integer> skuQuantityMap) {
        inventoryService.restoreStock(skuQuantityMap);
        return ResponseEntity.ok().build();
    }
}