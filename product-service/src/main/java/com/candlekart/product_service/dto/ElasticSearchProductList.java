package com.candlekart.product_service.dto;

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
    private List<ProductResponse> productsList;

    public List<ProductResponse> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductResponse> productsList) {
        this.productsList = productsList;
    }
}