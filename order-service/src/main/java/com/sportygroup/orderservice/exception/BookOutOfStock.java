package com.sportygroup.orderservice.exception;

public record BookOutOfStock(
        String id,
        int requiredQuantity,
        int availableQuantity) {

}
