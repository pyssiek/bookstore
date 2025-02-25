package com.sportygroup.orderservice.dto;

import java.math.BigDecimal;

public record BookDto(
        String id,
        String title,
        String author,
        String isbn,
        BookType type,
        BigDecimal price,
        int stockQuantity
) {
}
