package com.sportygroup.orderservice.exception;

public class CustomerNotFoundException extends BaseException {

    public CustomerNotFoundException(String id) {
        super("Customer with ID " + id + " not found", "CUSTOMER_NOT_FOUND");
    }
}