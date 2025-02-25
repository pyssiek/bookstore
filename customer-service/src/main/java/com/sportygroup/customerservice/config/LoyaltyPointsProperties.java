package com.sportygroup.customerservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("loyaltypoints")
public record LoyaltyPointsProperties(
        int numberOfLoyaltyPointsForFreeBook,
        int numberOfLoyaltyPointsPerBook
) {
}

