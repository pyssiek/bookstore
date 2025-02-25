package com.sportygroup.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
public record Order(
        @Id String orderId,
        String customerId,
        Instant orderDate,
        BigDecimal totalAmount,
        List<OrderItem> items
) {
}


