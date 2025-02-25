package com.sportygroup.orderservice.exception;

public class OrderNotFoundException extends BaseException {

    public OrderNotFoundException(String id) {
        super("Order with ID " + id + " not found", "ORDER_NOT_FOUND");
    }
}