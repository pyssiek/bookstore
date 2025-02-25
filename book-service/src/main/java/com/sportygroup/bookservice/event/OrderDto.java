package com.sportygroup.bookservice.event;

import java.util.List;

public record OrderDto(
        String orderId,
        List<OrderItemDto> items
) {
}
