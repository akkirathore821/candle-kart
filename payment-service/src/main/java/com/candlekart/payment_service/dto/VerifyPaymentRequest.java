package com.candlekart.payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyPaymentRequest {

    @NotBlank(message = "razorpay_order_id is required")
    private String razorpay_order_id;       // order_xxxx

    @NotBlank(message = "razorpay_payment_id is required")
    private String razorpay_payment_id;     // pay_xxxx

    @NotBlank(message = "razorpay_signature is required")
    private String razorpay_signature;      // HMAC-SHA256 signature
}