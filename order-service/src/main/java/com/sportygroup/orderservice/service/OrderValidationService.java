package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.PricingRulesProperties;
import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.CustomerDto;
import com.sportygroup.orderservice.dto.OrderItemRequestDto;
import com.sportygroup.orderservice.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderValidationService {

    private final PricingRulesProperties pricingRulesProperties;

    public OrderValidationService(PricingRulesProperties pricingRulesProperties) {
        this.pricingRulesProperties = pricingRulesProperties;
    }

    public void validateBooks(List<OrderItemRequestDto> items, Map<String, BookDto> books) {

        validateMissingBooks(items, books);

        validateOutOfStockBooks(items, books);

        validateBooksForLoyaltyPoints(items, books);
    }

    public void validateCustomerLoyaltyPoints(List<OrderItemRequestDto> items, CustomerDto customer) {

        int totalFreeQuantity = items.stream()
                .filter(OrderItemRequestDto::useLoyaltyPoints)
                .map(OrderItemRequestDto::quantity)
                .reduce(0, Integer::sum);

        int requiredLoyaltyPoints = totalFreeQuantity * pricingRulesProperties.numberOfLoyaltyPointsForFreeBook();
        if (customer.loyaltyPoints() < requiredLoyaltyPoints) {
            throw new CustomerLoyaltyPointsException(customer.id(), customer.loyaltyPoints(), requiredLoyaltyPoints);
        }
    }

    private void validateBooksForLoyaltyPoints(List<OrderItemRequestDto> items, Map<String, BookDto> books) {
        List<BookDto> invalidFreeBooks = items.stream()
                .filter(OrderItemRequestDto::useLoyaltyPoints)
                .map(item -> books.get(item.id()))
                .filter(bookDto -> !pricingRulesProperties.freeBookTypes().contains(bookDto.type()))
                .toList();

        if (!invalidFreeBooks.isEmpty()) {
            throw new BookNotForFreeException(invalidFreeBooks, pricingRulesProperties.freeBookTypes());

        }
    }

    private void validateOutOfStockBooks(List<OrderItemRequestDto> items, Map<String, BookDto> books) {

        List<BookOutOfStock> bookOutOfStocks = items.stream()
                .filter(item -> item.quantity() > books.get(item.id()).stockQuantity())
                .map(item -> new BookOutOfStock(item.id(), item.quantity(), books.get(item.id()).stockQuantity()))
                .toList();

        if (bookOutOfStocks.size() > 0) {
            throw new BookOutOfStockException(bookOutOfStocks);
        }
    }

    private void validateMissingBooks(List<OrderItemRequestDto> items, Map<String, BookDto> books) {
        List<String> missingBookIds = items.stream()
                .map(OrderItemRequestDto::id)
                .filter(id -> !books.containsKey(id))
                .toList();

        if (!missingBookIds.isEmpty()) {
            throw new BookNotFoundException(missingBookIds);
        }
    }

}
