package com.sportygroup.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        String id,
        String title,
        String isbn,
        BookType type,
        int quantity,
        BigDecimal originalPrice,
        int discountPercentage,
        boolean loyaltyPointsUsed,
        BigDecimal finalPrice,
        BigDecimal totalPrice
) {
}