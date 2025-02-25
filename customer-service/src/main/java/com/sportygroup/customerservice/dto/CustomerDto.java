package com.sportygroup.customerservice.dto;

import java.util.List;

public record CustomerDto(
        String id,
        String firstName,
        String lastName,
        String email,
        int loyaltyPoints,
        List<AddressDto> addresses
) {
}
