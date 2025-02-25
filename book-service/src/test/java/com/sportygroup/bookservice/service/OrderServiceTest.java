package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.event.OrderDto;
import com.sportygroup.bookservice.event.OrderItemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldUpdateStockForEachOrderItem() {
        // Given
        OrderItemDto item1 = new OrderItemDto(1L, 2);
        OrderItemDto item2 = new OrderItemDto(2L, 1);
        OrderDto order = new OrderDto("orderId", List.of(item1, item2));

        // When
        orderService.processOrder(order);

        // Then
        verify(bookService, times(1)).updateStock(1L, -2);
        verify(bookService, times(1)).updateStock(2L, -1);
        verifyNoMoreInteractions(bookService);
    }
}