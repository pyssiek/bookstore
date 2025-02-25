package com.sportygroup.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDto(
        @NotBlank(message = "Customer id cannot be empty")
        String customerId,
        @NotEmpty(message = "Order items list cannot be empty")
        @Valid
        List<OrderItemRequestDto> items) {
}
