package com.candlekart.order_service.service;

import com.candlekart.order_service.dto.CartResponse;
import com.candlekart.order_service.dto.OrderItemResponse;
import com.candlekart.order_service.dto.OrderResponse;
import com.candlekart.order_service.kafka.KafkaProducer;
import com.candlekart.order_service.model.*;
import com.candlekart.order_service.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.candlekart.order_service.constants.Constants.Create_Order_To_Inventory_Topic_Name;
import static com.candlekart.order_service.constants.Constants.Payment_Order_To_Payment_Topic_Name;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Transactional
    public OrderResponse createOrder(CartResponse cart){
        try{
            Order order = cartToOrderEntity(cart);
            OrderResponse orderDto = toDto(orderRepository.save(order));
            kafkaProducer.publish(Payment_Order_To_Payment_Topic_Name, orderDto);
            return orderDto;
        }catch (RuntimeException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<OrderResponse> getOrdersByUser(UUID userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::toDto).toList();
    }

    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not Found"));
        return toDto(order);
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
    }







    
    private OrderResponse toDto(Order order){
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .sku(item.getSku())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().toString())
                .items(itemResponses).build();
    }
    private Order cartToOrderEntity(CartResponse cart){

        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice()* item.getQuantity())
                .sum();

        Order order = Order.builder()
                .userId(UUID.fromString(cart.getUserId()))
                .status(OrderStatus.PENDING)
                .totalAmount(totalPrice)
                .createdAt(Instant.now())
                .build();

        List<OrderItem> items = cart.getItems().stream()
                .map(itemReq -> OrderItem.builder()
                        .sku(itemReq.getSku())
                        .quantity(itemReq.getQuantity())
                        .price(itemReq.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }
}
