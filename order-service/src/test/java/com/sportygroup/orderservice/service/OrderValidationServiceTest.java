package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.PricingRulesProperties;
import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.BookType;
import com.sportygroup.orderservice.dto.CustomerDto;
import com.sportygroup.orderservice.dto.OrderItemRequestDto;
import com.sportygroup.orderservice.exception.BookNotForFreeException;
import com.sportygroup.orderservice.exception.BookNotFoundException;
import com.sportygroup.orderservice.exception.BookOutOfStockException;
import com.sportygroup.orderservice.exception.CustomerLoyaltyPointsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidationServiceTest {

    @Mock
    private PricingRulesProperties pricingRulesProperties;

    @InjectMocks
    private OrderValidationService orderValidationService;

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1"
                , 2, false));
        Map<String, BookDto> books = Map.of();

        // When & Then
        assertThatThrownBy(() -> orderValidationService.validateBooks(items, books))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenBookOutOfStock() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1", 10, false));
        BookDto book = getBookDto();
        Map<String, BookDto> books = Map.of("1", book);

        // When & Then
        assertThatThrownBy(() -> orderValidationService.validateBooks(items, books))
                .isInstanceOf(BookOutOfStockException.class);
    }

    @Test
    void shouldThrowExceptionWhenBookNotEligibleForLoyaltyPoints() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1", 1, true));
        BookDto book = getBookDto();
        Map<String, BookDto> books = Map.of("1", book);

        when(pricingRulesProperties.freeBookTypes()).thenReturn(List.of(BookType.REGULAR, BookType.OLD_EDITION));

        // When & Then
        assertThatThrownBy(() -> orderValidationService.validateBooks(items, books))
                .isInstanceOf(BookNotForFreeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCustomerHasInsufficientLoyaltyPoints() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1", 1, true));
        CustomerDto customer = new CustomerDto("customer1", 9);

        when(pricingRulesProperties.numberOfLoyaltyPointsForFreeBook()).thenReturn(10);

        // When & Then
        assertThatThrownBy(() -> orderValidationService.validateCustomerLoyaltyPoints(items, customer))
                .isInstanceOf(CustomerLoyaltyPointsException.class);
    }

    @Test
    void shouldPassValidationWhenBooksAreValid() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1", 2, false));
        BookDto book = getBookDto();
        Map<String, BookDto> books = Map.of("1", book);

        // When & Then
        assertThatCode(() -> orderValidationService.validateBooks(items, books))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidationWhenCustomerHasEnoughLoyaltyPoints() {
        // Given
        List<OrderItemRequestDto> items = List.of(new OrderItemRequestDto("1"
                , 2, true));
        CustomerDto customer = new CustomerDto("customer1", 20);

        when(pricingRulesProperties.numberOfLoyaltyPointsForFreeBook()).thenReturn(10);

        // When & Then
        assertThatCode(() -> orderValidationService.validateCustomerLoyaltyPoints(items, customer))
                .doesNotThrowAnyException();
    }

    private BookDto getBookDto() {
        return new BookDto("1", "Effective Java", "Joshua Bloch", "123456789",
                BookType.NEW_RELEASE, new BigDecimal("145.99"), 5);
    }
}