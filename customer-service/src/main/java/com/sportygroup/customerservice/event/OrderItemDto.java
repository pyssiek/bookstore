package com.sportygroup.customerservice.event;

public record OrderItemDto(
        Long id,
        int quantity,
        boolean loyaltyPointsUsed
) {
}