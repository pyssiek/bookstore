package com.sportygroup.customerservice.service;

import com.sportygroup.customerservice.dto.CustomerDto;
import com.sportygroup.customerservice.dto.CustomerPageResponseDto;
import com.sportygroup.customerservice.exception.CustomerNotFoundException;
import com.sportygroup.customerservice.mapper.CustomerMapper;
import com.sportygroup.customerservice.model.Customer;
import com.sportygroup.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDto getCustomerById(String customerId) {
        return customerMapper.toDto(getCustomer(customerId));
    }

    public CustomerPageResponseDto getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerMapper.toCustomerPageResponseDto(customerPage);
    }

    public String addCustomer(CustomerDto customer) {
        return customerRepository.save(customerMapper.toEntity(customer)).id();
    }

    public CustomerDto updateLoyaltyPoints(String customerId, int points) {
        customerRepository.updateLoyaltyPoints(customerId, points);
        return customerMapper.toDto(getCustomer(customerId));
    }

    private Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

}