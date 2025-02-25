package com.sportygroup.customerservice.exception;

import lombok.Getter;

@Getter
public class CustomerNotFoundException extends RuntimeException {
    private final String errorCode = "CUSTOMER_NOT_FOUND";

    public CustomerNotFoundException(String id) {
        super("Customer with ID " + id + " not found");
    }
}