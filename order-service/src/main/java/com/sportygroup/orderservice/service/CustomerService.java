package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.client.CustomerServiceClient;
import com.sportygroup.orderservice.dto.CustomerDto;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerServiceClient customerServiceClient;

    public CustomerService(CustomerServiceClient customerServiceClient) {
        this.customerServiceClient = customerServiceClient;
    }

    public CustomerDto getCustomer(String customerId) {
        return customerServiceClient.getCustomer(customerId);
    }
}
