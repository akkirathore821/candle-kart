package com.candlekart.payment_service.controller;

import com.candlekart.payment_service.dto.CreateOrderRequest;
import com.candlekart.payment_service.dto.CreateOrderResponse;
import com.candlekart.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ──────────────────────────────────────────────────────────────────────────
    // POST /api/payment/create-order
    // Called by frontend BEFORE opening Razorpay popup
    // Returns: { orderId, amount, currency, keyId, receipt }
    // ──────────────────────────────────────────────────────────────────────────
    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal Long userId) {

        log.info("Creating order for user: {} amount: {} paise", userId, request.getAmount());

        CreateOrderResponse response = paymentService.createOrder(request, userId);
        return ResponseEntity.ok(response);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // POST /api/payment/verify
    // Called by frontend AFTER Razorpay popup succeeds
    // Verifies HMAC signature and marks payment as SUCCESS
    // ──────────────────────────────────────────────────────────────────────────
    @PostMapping("/verify")
    public ResponseEntity<VerifyPaymentResponse> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request,
            @AuthenticationPrincipal Long userId) {

        log.info("Verifying payment for order: {} by user: {}",
                request.getRazorpay_order_id(), userId);

        VerifyPaymentResponse response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(response);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // GET /api/payment/history
    // Returns all payments for the logged-in user
    // ──────────────────────────────────────────────────────────────────────────
    @GetMapping("/history")
    public ResponseEntity<List<Payment>> getPaymentHistory(
            @AuthenticationPrincipal Long userId) {

        log.info("Fetching payment history for user: {}", userId);
        return ResponseEntity.ok(paymentService.getUserPayments(userId));
    }
}