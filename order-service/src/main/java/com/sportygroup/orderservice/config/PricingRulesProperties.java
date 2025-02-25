package com.sportygroup.orderservice.config;

import com.sportygroup.orderservice.dto.BookType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("pricing-rules")
public record PricingRulesProperties(
        int numberOfLoyaltyPointsForFreeBook,
        List<BookType> freeBookTypes,
        Map<BookType, DiscountRule> discountRules
) {
}

