package com.sportygroup.orderservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(
        String orderId,
        String customerId,
        Instant orderDate,
        BigDecimal totalAmount,
        List<OrderItemDto> items
) {
}
