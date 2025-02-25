package com.sportygroup.orderservice.mapper;

import com.sportygroup.orderservice.dto.BookType;
import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.dto.OrderPageResponseDto;
import com.sportygroup.orderservice.model.Order;
import com.sportygroup.orderservice.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

    private OrderMapper orderMapper = new OrderMapper();

    @Mock
    private Page<Order> orderPage;

    @Test
    void shouldMapOrderToOrderDto() {
        // Given

        Order order = getOrder();

        // When
        OrderDto orderDto = orderMapper.toDto(order);

        // Then
        assertThat(orderDto.orderId()).isEqualTo(order.orderId());
        assertThat(orderDto.customerId()).isEqualTo(order.customerId());
        assertThat(orderDto.orderDate()).isEqualTo(order.orderDate());
        assertThat(orderDto.totalAmount()).isEqualTo(order.totalAmount());
        assertThat(orderDto.items()).hasSize(1);
        assertThat(orderDto.items().get(0).id()).isEqualTo(order.items().get(0).id());
    }

    @Test
    void shouldMapPageOfOrdersToOrderPageResponseDto() {
        // Given
        List<Order> orders = List.of(getOrder());
        PageImpl<Order> orderPage = new PageImpl<>(orders);

        // When
        OrderPageResponseDto responseDto = orderMapper.toOrderPageResponseDto(orderPage);

        // Then
        assertThat(responseDto.orders().size()).isEqualTo(1);
        assertThat(responseDto.totalElements()).isEqualTo(orderPage.getTotalElements());
        assertThat(responseDto.totalPages()).isEqualTo(orderPage.getTotalElements());
        assertThat(responseDto.currentPage()).isEqualTo(orderPage.getNumber() + 1);
        assertThat(responseDto.pageSize()).isEqualTo(orderPage.getSize());

    }

    private Order getOrder() {
        Order order = new Order("order1", "customer1", Instant.now(),
                BigDecimal.TEN, List.of(getOrderItem()));
        return order;
    }

    private OrderItem getOrderItem() {
        return new OrderItem("1", "Thinking in Java", "123456",
                BookType.NEW_RELEASE, 2
                , BigDecimal.TEN, 10, false, BigDecimal.TEN,
                BigDecimal.TEN);
    }
}