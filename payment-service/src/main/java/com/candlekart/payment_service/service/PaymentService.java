package com.candlekart.payment_service.service;

import com.candlekart.payment_service.dto.CreateOrderRequest;
import com.candlekart.payment_service.dto.CreateOrderResponse;
import com.candlekart.payment_service.dto.VerifyPaymentRequest;
import com.candlekart.payment_service.dto.VerifyPaymentResponse;
import com.candlekart.payment_service.model.Payment;
import com.candlekart.payment_service.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient  razorpayClient;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    // ──────────────────────────────────────────────────────────────────────────
    // STEP 1 — Create Razorpay Order
    // ──────────────────────────────────────────────────────────────────────────
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, Long userId) {
        try {
            // 1a. Generate internal receipt ID
            String receipt = "rcpt_" + UUID.randomUUID().toString().substring(0, 8);

            // 1b. Build Razorpay order options
            JSONObject options = new JSONObject();
            options.put("amount",   request.getAmount());           // in paise
            options.put("currency", request.getCurrency());
            options.put("receipt",  receipt);
            options.put("payment_capture", 1);                      // auto-capture

            // Optional: add notes
            JSONObject notes = new JSONObject();
            notes.put("userId",    userId.toString());
            notes.put("addressId", request.getAddressId().toString());
            options.put("notes", notes);

            // 1c. Call Razorpay API to create order
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);
            String razorpayOrderId = razorpayOrder.get("id");

            log.info("Razorpay order created: {} for user: {}", razorpayOrderId, userId);

            // 1d. Save to DB with CREATED status
            Payment payment = Payment.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .userId(userId)
                    .addressId(request.getAddressId())
                    .receipt(receipt)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(Payment.PaymentStatus.CREATED)
                    .build();

            paymentRepository.save(payment);

            // 1e. Return response to frontend
            return CreateOrderResponse.builder()
                    .orderId(razorpayOrderId)       // order_xxxx
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .keyId(razorpayKeyId)           // rzp_test_xxxx — safe to expose
                    .receipt(receipt)
                    .build();

        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order: {}", e.getMessage());
            throw new RuntimeException("Failed to create payment order: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // STEP 2 — Verify Payment Signature
    // ──────────────────────────────────────────────────────────────────────────
    @Transactional
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) {

        // 2a. Find payment record by Razorpay order ID
        Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpay_order_id())
                .orElseThrow(() -> new RuntimeException(
                        "Payment record not found for order: " + request.getRazorpay_order_id()));

        // 2b. Verify HMAC-SHA256 signature
        // Razorpay signature = HMAC_SHA256(orderId + "|" + paymentId, keySecret)
        boolean isValid = verifySignature(
                request.getRazorpay_order_id(),
                request.getRazorpay_payment_id(),
                request.getRazorpay_signature()
        );

        if (!isValid) {
            // Mark as failed
            payment.setStatus(Payment.PaymentStatus.FAILED);
            paymentRepository.save(payment);
            log.warn("Signature verification FAILED for order: {}", request.getRazorpay_order_id());
            throw new RuntimeException("Payment signature verification failed. Possible fraud attempt.");
        }

        // 2c. Update record with payment details
        payment.setRazorpayPaymentId(request.getRazorpay_payment_id());
        payment.setRazorpaySignature(request.getRazorpay_signature());
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        log.info("Payment verified successfully: {} for order: {}",
                request.getRazorpay_payment_id(), request.getRazorpay_order_id());

        return VerifyPaymentResponse.builder()
                .success(true)
                .message("Payment verified successfully")
                .paymentId(request.getRazorpay_payment_id())
                .orderId(request.getRazorpay_order_id())
                .amount(payment.getAmount())
                .build();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Get payment history for a user
    // ──────────────────────────────────────────────────────────────────────────
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // HMAC-SHA256 Signature Verification
    // ──────────────────────────────────────────────────────────────────────────
    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            // Razorpay's formula: HMAC_SHA256(orderId + "|" + paymentId, secret)
            String payload = orderId + "|" + paymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    razorpayKeySecret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            ));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hex string
            String generatedSignature = HexFormat.of().formatHex(hash);

            return generatedSignature.equals(signature);

        } catch (Exception e) {
            log.error("Signature verification error: {}", e.getMessage());
            return false;
        }
    }
}
