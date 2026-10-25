package com.candlekart.payment_service.dto;

import lombok.*;

// ── Response: sent after successful signature verification ────────────────────
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPaymentResponse {

    private boolean success;
    private String  message;
    private String  paymentId;      // pay_xxxx
    private String  orderId;        // order_xxxx
    private Long    amount;         // in paise
}