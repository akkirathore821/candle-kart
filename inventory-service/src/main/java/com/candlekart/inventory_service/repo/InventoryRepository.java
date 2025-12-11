package com.candlekart.inventory_service.repo;

import com.candlekart.inventory_service.model.InventoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryProduct, Long> {
    Optional<InventoryProduct> findBySkuCode(String skuCode);
    List<InventoryProduct> findBySkuCodeIn(List<String> skuCodes);
}