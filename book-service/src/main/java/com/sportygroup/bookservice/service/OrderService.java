package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.event.OrderDto;
import com.sportygroup.bookservice.event.OrderItemDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final BookService bookService;

    @Transactional
    public void processOrder(OrderDto order) {
        for (OrderItemDto item : order.items()) {
            log.info("Updating stock. Book id: {}, quantity: {}", item.id(), -item.quantity());
            bookService.updateStock(item.id(), -item.quantity());
        }
    }

}
