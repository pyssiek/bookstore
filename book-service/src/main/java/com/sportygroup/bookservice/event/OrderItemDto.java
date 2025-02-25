package com.sportygroup.bookservice.event;

public record OrderItemDto(
        Long id,
        int quantity
) {
}