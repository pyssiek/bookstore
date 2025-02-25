package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.config.RabbitMQConfig;
import com.sportygroup.bookservice.event.EventEnvelope;
import com.sportygroup.bookservice.event.OrderDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderListener {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.BOOK_SERVICE_QUEUE)
    public void handleOrderEvent(EventEnvelope<OrderDto> event) {
        MDC.pushByKey("traceId", event.traceId());
        log.info("Processing order: " + event);
        orderService.processOrder(event.payload());
    }
}
