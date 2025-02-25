package com.sportygroup.customerservice.event;

import java.util.List;

public record OrderDto(
        String orderId,
        String customerId,
        List<OrderItemDto> items
) {
}
