package com.sportygroup.customerservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customers")
public record Customer(
        @Id String id,
        String firstName,
        String lastName,
        String email,
        int loyaltyPoints,
        List<Address> addresses
) {
}