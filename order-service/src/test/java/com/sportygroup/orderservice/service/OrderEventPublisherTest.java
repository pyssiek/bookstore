package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.RabbitMQConfig;
import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.event.EventEnvelope;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEventPublisherTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    @InjectMocks
    private OrderEventPublisher orderEventPublisher;


    @Test
    void shouldPublishOrderEvent() {
        // Given
        OrderDto orderDto = new OrderDto("1", "customer1", Instant.now(),
                BigDecimal.valueOf(99.99), List.of());
        EventEnvelope<OrderDto> expectedEvent = EventEnvelope.wrap(orderDto);

        // When
        orderEventPublisher.publishOrderEvent(orderDto);

        // Then
        verify(amqpTemplate).convertAndSend(eq(RabbitMQConfig.ORDER_EXCHANGE), eq(""), eq(expectedEvent));
    }
}