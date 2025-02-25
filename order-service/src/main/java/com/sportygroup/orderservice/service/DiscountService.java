package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.config.DiscountRule;
import com.sportygroup.orderservice.config.PricingRulesProperties;
import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.BookType;
import com.sportygroup.orderservice.dto.OrderItemRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DiscountService {

    private final PricingRulesProperties pricingRulesProperties;

    public DiscountService(PricingRulesProperties pricingRulesProperties) {
        this.pricingRulesProperties = pricingRulesProperties;
        log.info("Price rules: " + pricingRulesProperties);
    }

    public Map<String, Integer> calculate(List<OrderItemRequestDto> orderItems, Map<String, BookDto> books) {
        Map<BookType, DiscountRule> discountRulesMap = pricingRulesProperties.discountRules();
        Map<String, Integer> discounts = new HashMap<>();

        int totalBooks = orderItems.stream()
                .map(OrderItemRequestDto::quantity)
                .reduce(0, Integer::sum);

        for (var orderItem : orderItems) {
            if (orderItem.useLoyaltyPoints()) {
                discounts.put(orderItem.id(), 100);
                continue;
            }

            BookDto book = books.get(orderItem.id());
            DiscountRule discountRule = discountRulesMap.get(book.type());
            if (discountRule != null) {
                int discount = discountRule.discount();
                if (totalBooks >= discountRule.bundle()) {
                    discount += discountRule.bundleDiscount();
                }
                discounts.put(orderItem.id(), discount);
            } else {
                log.warn("No pricing rules for type: " + book.type());
                discounts.put(orderItem.id(), 0);
            }
        }
        return discounts;
    }
}
