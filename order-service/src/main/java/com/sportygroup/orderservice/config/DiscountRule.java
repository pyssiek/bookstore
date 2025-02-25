package com.sportygroup.orderservice.config;

public record DiscountRule(
        int discount,
        int bundle,
        int bundleDiscount) {

}
