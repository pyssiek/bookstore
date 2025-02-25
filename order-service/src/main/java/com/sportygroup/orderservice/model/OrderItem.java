package com.sportygroup.orderservice.model;

import com.sportygroup.orderservice.dto.BookType;

import java.math.BigDecimal;

public record OrderItem(
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