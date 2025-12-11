package com.candlekart.inventory_service.kafka;

import com.candlekart.inventory_service.dto.OrderResponse;
import com.candlekart.inventory_service.exc.InvalidOrderException;
import com.candlekart.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.candlekart.inventory_service.constants.Constants.Create_Order_To_Inventory_Topic_Name;

@Component
public class KafkaConsumer {

    @Autowired
    private InventoryService inventoryService;

    @KafkaListener(topics = Create_Order_To_Inventory_Topic_Name)
    public void onMessage(OrderResponse order){

        if(order == null) throw new InvalidOrderException("Kafka Order is null or invalid data");

        inventoryService.onOrderCreation(order);

    }

}
