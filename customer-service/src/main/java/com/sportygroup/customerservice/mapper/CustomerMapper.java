package com.sportygroup.customerservice.mapper;

import com.sportygroup.customerservice.dto.AddressDto;
import com.sportygroup.customerservice.dto.CustomerDto;
import com.sportygroup.customerservice.dto.CustomerPageResponseDto;
import com.sportygroup.customerservice.model.Address;
import com.sportygroup.customerservice.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer customer) {
        List<AddressDto> addressDtos = customer.addresses().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new CustomerDto(
                customer.id(),
                customer.firstName(),
                customer.lastName(),
                customer.email(),
                customer.loyaltyPoints(),
                addressDtos
        );
    }

    private AddressDto toDto(Address address) {
        return new AddressDto(
                address.street(),
                address.city(),
                address.postalCode(),
                address.country(),
                address.type()
        );
    }

    public Customer toEntity(CustomerDto customerDto) {
        List<Address> addresses = customerDto.addresses().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        return new Customer(
                customerDto.id(),
                customerDto.firstName(),
                customerDto.lastName(),
                customerDto.email(),
                customerDto.loyaltyPoints(),
                addresses
        );
    }

    private Address toEntity(AddressDto addressDto) {
        return new Address(
                addressDto.street(),
                addressDto.city(),
                addressDto.postalCode(),
                addressDto.country(),
                addressDto.type()
        );
    }

    public CustomerPageResponseDto toCustomerPageResponseDto(Page<Customer> customerPage) {
        List<CustomerDto> customers = customerPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new CustomerPageResponseDto(
                customers,
                customerPage.getTotalElements(),
                customerPage.getTotalPages(),
                customerPage.getNumber() + 1,
                customerPage.getSize()
        );
    }
}
