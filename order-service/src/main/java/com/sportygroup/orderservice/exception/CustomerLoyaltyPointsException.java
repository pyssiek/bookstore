package com.sportygroup.orderservice.exception;

public class CustomerLoyaltyPointsException extends BaseException {

    public CustomerLoyaltyPointsException(String id, int customerPoints, int requiredPoints) {
        super(String.format("Not enough loyalty points. Customer id: %s, points: %d, required points: %d", id, customerPoints, requiredPoints),
                "CUSTOMER_INVALID_LOYALTY_POINTS");
    }
}