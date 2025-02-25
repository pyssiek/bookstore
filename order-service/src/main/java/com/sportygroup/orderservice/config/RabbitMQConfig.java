package com.sportygroup.orderservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String BOOK_SERVICE_QUEUE = "book-service-queue";
    public static final String CUSTOMER_SERVICE_QUEUE = "customer-service-queue";

    @Bean
    public FanoutExchange orderExchange() {
        return new FanoutExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue bookServiceQueue() {
        return new Queue(BOOK_SERVICE_QUEUE, true);
    }

    @Bean
    public Queue customerServiceQueue() {
        return new Queue(CUSTOMER_SERVICE_QUEUE, true);
    }

    @Bean
    public Binding bookServiceBinding(Queue bookServiceQueue, FanoutExchange orderExchange) {
        return BindingBuilder.bind(bookServiceQueue).to(orderExchange);
    }

    @Bean
    public Binding customerServiceBinding(Queue customerServiceQueue, FanoutExchange orderExchange) {
        return BindingBuilder.bind(customerServiceQueue).to(orderExchange);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
