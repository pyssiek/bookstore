package com.sportygroup.customerservice.service;

import com.sportygroup.customerservice.config.LoyaltyPointsProperties;
import com.sportygroup.customerservice.event.OrderDto;
import com.sportygroup.customerservice.event.OrderItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final CustomerService customerService;
    private final LoyaltyPointsProperties loyaltyPointsProperties;

    public OrderService(CustomerService customerService, LoyaltyPointsProperties loyaltyPointsProperties) {
        this.customerService = customerService;
        this.loyaltyPointsProperties = loyaltyPointsProperties;
    }

    public void processOrder(OrderDto order) {
        int loyaltyPoints = 0;
        for (OrderItemDto item : order.items()) {
            if (item.loyaltyPointsUsed()) {
                loyaltyPoints -= item.quantity() * loyaltyPointsProperties.numberOfLoyaltyPointsForFreeBook();
                continue;
            }
            loyaltyPoints += item.quantity() * loyaltyPointsProperties.numberOfLoyaltyPointsPerBook();
        }
        log.info("Updating loyalty points. Customer id: {}, points, {}",
                order.customerId(), loyaltyPoints);
        customerService.updateLoyaltyPoints(order.customerId(), loyaltyPoints);
    }

}
