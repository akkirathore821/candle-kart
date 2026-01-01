package com.candlekart.inventory_service.repo;

import com.candlekart.inventory_service.model.InventoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryProduct, UUID> {
    Optional<InventoryProduct> findBySku(String sku);
    List<InventoryProduct> findBySkuIn(List<String> skus);
}