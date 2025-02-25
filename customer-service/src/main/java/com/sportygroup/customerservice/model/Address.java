package com.sportygroup.customerservice.model;

public record Address(
        String street,
        String city,
        String postalCode,
        String country,
        AddressType type
) {
}