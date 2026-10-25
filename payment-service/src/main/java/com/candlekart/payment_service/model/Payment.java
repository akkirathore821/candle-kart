package com.candlekart.payment_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Razorpay IDs ──────────────────────────────────────────────────────────
    @Column(name = "razorpay_order_id", nullable = false, unique = true)
    private String razorpayOrderId;         // order_xxxx  — created by us

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;       // pay_xxxx  — set after user pays

    @Column(name = "razorpay_signature")
    private String razorpaySignature;       // signature — set after verification

    // ── Order Info ────────────────────────────────────────────────────────────
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @Column(name = "receipt", nullable = false)
    private String receipt;                 // your internal receipt ID

    // ── Amount ────────────────────────────────────────────────────────────────
    @Column(nullable = false)
    private Long amount;                    // in paise

    @Column(nullable = false, length = 3)
    private String currency;               // INR

    // ── Status ────────────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // ── Timestamps ────────────────────────────────────────────────────────────
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ── Status Enum ───────────────────────────────────────────────────────────
    public enum PaymentStatus {
        CREATED,        // order created, user hasn't paid yet
        SUCCESS,        // payment verified successfully
        FAILED,         // payment failed
        CANCELLED       // user dismissed the popup
    }
}