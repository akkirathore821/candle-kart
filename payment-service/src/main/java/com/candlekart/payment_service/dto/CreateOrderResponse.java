package com.candlekart.payment_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderResponse {
    private String orderId;     // order_xxxx  — Razorpay order ID
    private Long   amount;      // in paise
    private String currency;    // INR
    private String keyId;       // rzp_test_xxx — public key, safe for frontend
    private String receipt;     // your internal receipt
}