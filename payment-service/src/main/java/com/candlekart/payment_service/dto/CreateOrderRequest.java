package com.candlekart.payment_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotNull(message = "Amount is required")
    @Min(value = 100, message = "Minimum amount is ₹1 (100 paise)")
    private Long amount;                // in paise

    @NotBlank(message = "Currency is required")
    private String currency = "INR";

    @NotNull(message = "Address ID is required")
    private Long addressId;

    private List<OrderItem> items;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class OrderItem {
        private Long id;
        private Integer quantity;
    }
}