package com.candlekart.order_service.service;

import com.candlekart.order_service.dto.CartResponse;
import com.candlekart.order_service.dto.OrderItemResponse;
import com.candlekart.order_service.dto.OrderResponse;
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

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderResponse createOrder(CartResponse cart){

        Order order = cartToOrderEntity(cart);

        Order saved = orderRepository.save(order);

        return toDto(saved);
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
                .userId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().toString())
                .items(itemResponses).build();
    }
    private Order cartToOrderEntity(CartResponse cart){
        Order order = Order.builder()
                .userId(UUID.fromString(cart.getUserId()))
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
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
        order.setTotalAmount(
                items.stream()
                        .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        return order;
    }
}
