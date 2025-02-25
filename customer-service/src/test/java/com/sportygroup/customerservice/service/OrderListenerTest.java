package com.sportygroup.customerservice.service;

import com.sportygroup.customerservice.event.EventEnvelope;
import com.sportygroup.customerservice.event.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderListenerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderListener orderListener;

    private EventEnvelope<OrderDto> event;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        orderDto = new OrderDto(null, null, null);
        event = new EventEnvelope<>("trace12345", orderDto);
    }

    @Test
    void shouldHandleOrderEvent() {

        //when
        orderListener.handleOrderEvent(event);

        //then
        verify(orderService, times(1)).processOrder(orderDto);
    }
}