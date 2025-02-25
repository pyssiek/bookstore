package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.DiscountRule;
import com.sportygroup.orderservice.config.PricingRulesProperties;
import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.BookType;
import com.sportygroup.orderservice.dto.OrderItemRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private PricingRulesProperties pricingRulesProperties;

    @InjectMocks
    private DiscountService discountService;

    @Test
    void shouldApply100PercentDiscountForLoyaltyPoints() {
        // Given
        List<OrderItemRequestDto> orderItems = List.of(
                new OrderItemRequestDto("1", 1, true)
        );

        Map<String, BookDto> books = Map.of("1", getBookDto());

        // When
        Map<String, Integer> discounts = discountService.calculate(orderItems, books);

        // Then
        assertThat(discounts).containsEntry("1", 100);
    }

    @Test
    void shouldApplyStandardDiscountForBookType() {
        // Given
        DiscountRule discountRule = new DiscountRule(10, 3, 5);

        when(pricingRulesProperties.discountRules()).thenReturn(Map.of(BookType.OLD_EDITION, discountRule));

        List<OrderItemRequestDto> orderItems = List.of(
                new OrderItemRequestDto("1", 1, false)
        );

        Map<String, BookDto> books = Map.of("1", getBookDto());

        // When
        Map<String, Integer> discounts = discountService.calculate(orderItems, books);

        // Then
        assertThat(discounts).containsEntry("1", 10);
    }

    @Test
    void shouldApplyBundleDiscountWhenTotalBooksReachThreshold() {
        // Given
        DiscountRule regularDiscountRule = new DiscountRule(10, 3, 3);
        DiscountRule oldEditionDiscountRule = new DiscountRule(20, 3, 5);

        when(pricingRulesProperties.discountRules()).thenReturn(
                Map.of(BookType.OLD_EDITION, oldEditionDiscountRule,
                        BookType.REGULAR, regularDiscountRule)
        );

        List<OrderItemRequestDto> orderItems = List.of(
                new OrderItemRequestDto("1", 5, false),
                new OrderItemRequestDto("2", 1, false)
        );

        Map<String, BookDto> books = Map.of(
                "1", getBookDto(),
                "2", getRegularBookDto()
        );

        // When
        Map<String, Integer> discounts = discountService.calculate(orderItems, books);

        // Then
        assertThat(discounts).containsEntry("1", 25);
        assertThat(discounts).containsEntry("2", 13);
    }

    @Test
    void shouldApplyZeroDiscountWhenBookTypeHasNoRules() {
        // Given
        when(pricingRulesProperties.discountRules()).thenReturn(Map.of()); // No discount rules defined

        List<OrderItemRequestDto> orderItems = List.of(
                new OrderItemRequestDto("1", 1, false)
        );

        Map<String, BookDto> books = Map.of("1", getBookDto());

        // When
        Map<String, Integer> discounts = discountService.calculate(orderItems, books);

        // Then
        assertThat(discounts).containsEntry("1", 0);
    }
    
    private BookDto getBookDto() {
        return new BookDto("1", "Effective Java", "Joshua Bloch", "123456789",
                BookType.OLD_EDITION, new BigDecimal("145.99"), 5);
    }

    private BookDto getRegularBookDto() {
        return new BookDto("2", "Effective Java", "Joshua Bloch", "123456789",
                BookType.REGULAR, new BigDecimal("145.99"), 5);
    }
}