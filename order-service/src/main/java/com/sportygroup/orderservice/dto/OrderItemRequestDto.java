package com.sportygroup.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(
        @NotBlank(message = "Order item id cannot be empty")
        String id,
        @NotNull(message = "Quantity cannot be empty")
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity,
        boolean useLoyaltyPoints) {
}
