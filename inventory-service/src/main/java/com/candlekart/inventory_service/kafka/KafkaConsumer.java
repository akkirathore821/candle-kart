package com.candlekart.inventory_service.kafka;

import com.candlekart.inventory_service.dto.InventoryRequest;
import com.candlekart.inventory_service.dto.InventoryRequestList;
import com.candlekart.inventory_service.dto.OrderResponse;
import com.candlekart.inventory_service.exc.InvalidOrderException;
import com.candlekart.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.candlekart.inventory_service.constants.Constants.*;

@Component
public class KafkaConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = Create_Order_To_Inventory_Topic_Name)
    public void onMessage(OrderResponse order){
        if(order == null) throw new InvalidOrderException("Kafka Order is null or invalid data");
        inventoryService.onOrderCreation(order);

    }

    @KafkaListener(topics = Reserve_Order_To_Inventory_Topic_Name)
    public void onMessage(InventoryRequestList ordersList){
        if(ordersList == null || ordersList.isEmpty()) throw new InvalidOrderException("Kafka Order is null or invalid data");
        inventoryService.reserveStock(ordersList);

    }

    @KafkaListener(topics = Release_Stock_To_Inventory_Topic_Name)
    public void onMessage(InventoryRequestList ordersList){
        if(ordersList == null || ordersList.isEmpty()) throw new InvalidOrderException("Kafka Order is null or invalid data");
        inventoryService.releaseStock(ordersList);

    }

}
