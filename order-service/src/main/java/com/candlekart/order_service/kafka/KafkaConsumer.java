package com.candlekart.order_service.kafka;

import com.candlekart.order_service.dto.CartResponse;
import com.candlekart.order_service.dto.OrderResponse;
import com.candlekart.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.candlekart.order_service.constants.Constants.CART_CHECKOUT_TOPIC;

@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = CART_CHECKOUT_TOPIC)
    public void onMessage(CartResponse cart){

        if(cart == null) throw new RuntimeException();

        OrderResponse response = orderService.createOrder(cart);

    }

}
