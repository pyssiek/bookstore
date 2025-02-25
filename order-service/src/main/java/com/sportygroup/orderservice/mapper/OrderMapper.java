package com.sportygroup.orderservice.mapper;

import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.dto.OrderItemDto;
import com.sportygroup.orderservice.dto.OrderPageResponseDto;
import com.sportygroup.orderservice.model.Order;
import com.sportygroup.orderservice.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        List<OrderItemDto> orderItemDtos = order.items().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new OrderDto(
                order.orderId(),
                order.customerId(),
                order.orderDate(),
                order.totalAmount(),
                orderItemDtos
        );
    }

    public OrderPageResponseDto toOrderPageResponseDto(Page<Order> orderPage) {
        List<OrderDto> orders = orderPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new OrderPageResponseDto(
                orders,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getNumber() + 1,
                orderPage.getSize()
        );
    }

    private OrderItemDto toDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.id(),
                orderItem.title(),
                orderItem.isbn(),
                orderItem.type(),
                orderItem.quantity(),
                orderItem.originalPrice(),
                orderItem.discountPercentage(),
                orderItem.loyaltyPointsUsed(),
                orderItem.finalPrice(),
                orderItem.totalPrice()
        );
    }
}
