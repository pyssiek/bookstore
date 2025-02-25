package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.RabbitMQConfig;
import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.event.EventEnvelope;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

    private final AmqpTemplate amqpTemplate;

    public OrderEventPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publishOrderEvent(OrderDto order) {
        EventEnvelope<OrderDto> orderCreatedEvent = EventEnvelope.wrap(order);
        amqpTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, "", orderCreatedEvent);
    }
}
