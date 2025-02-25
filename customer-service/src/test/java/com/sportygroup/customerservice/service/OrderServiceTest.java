package com.sportygroup.customerservice.service;

import com.sportygroup.customerservice.config.LoyaltyPointsProperties;
import com.sportygroup.customerservice.event.OrderDto;
import com.sportygroup.customerservice.event.OrderItemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private LoyaltyPointsProperties loyaltyPointsProperties;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldIncreaseLoyaltyPointsWhenOrderIsProcessed() {
        // Given
        OrderDto order = getOrder();

        when(loyaltyPointsProperties.numberOfLoyaltyPointsPerBook()).thenReturn(1);

        // When
        orderService.processOrder(order);

        // Then
        ArgumentCaptor<Integer> pointsCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(customerService).updateLoyaltyPoints(eq("customer1"),
                pointsCaptor.capture());
        assertThat(pointsCaptor.getValue()).isEqualTo(1);
    }


    @Test
    void shouldDecreaseLoyaltyPointsWhenOrderUsesLoyaltyPoints() {
        // Given
        OrderDto order = new OrderDto("order1", "customer1",
                List.of(new OrderItemDto(1L, 3, true)));

        when(loyaltyPointsProperties.numberOfLoyaltyPointsForFreeBook()).thenReturn(10);

        // When
        orderService.processOrder(order);

        // Then
        ArgumentCaptor<Integer> pointsCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(customerService).updateLoyaltyPoints(eq("customer1"), pointsCaptor.capture());

        assertThat(pointsCaptor.getValue()).isEqualTo(-30);
    }

    private OrderDto getOrder() {
        return new OrderDto("order1", "customer1",
                List.of(new OrderItemDto(1L, 1, false)));
    }
}