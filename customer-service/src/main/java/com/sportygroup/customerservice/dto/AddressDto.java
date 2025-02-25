package com.sportygroup.customerservice.dto;

import com.sportygroup.customerservice.model.AddressType;

public record AddressDto(
        String street,
        String city,
        String postalCode,
        String country,
        AddressType type
) {
}