package com.candlekart.elasticsearch_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElasticSearchProductList {
    private List<ProductRequest> productsList;

    public List<ProductRequest> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductRequest> productsList) {
        this.productsList = productsList;
    }
}