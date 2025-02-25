package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.event.EventEnvelope;
import com.sportygroup.bookservice.event.OrderDto;
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
        orderDto = new OrderDto(null, null);
        event = new EventEnvelope<>("trace-12345", orderDto);
    }

    @Test
    void shouldHandleOrderEvent() {

        //when
        orderListener.handleOrderEvent(event);

        //then
        verify(orderService, times(1)).processOrder(orderDto);
    }
}