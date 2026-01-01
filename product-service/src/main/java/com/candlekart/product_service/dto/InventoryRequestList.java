package com.candlekart.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestList {
    private List<InventoryRequest> orderList;

    public List<InventoryRequest> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<InventoryRequest> orderList) {
        this.orderList = orderList;
    }
}
