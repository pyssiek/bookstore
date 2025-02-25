package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.client.CustomerServiceClient;
import com.sportygroup.orderservice.dto.CustomerDto;
import com.sportygroup.orderservice.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerServiceClient customerServiceClient;

    @InjectMocks
    private CustomerService customerService;


    @Test
    void shouldReturnCustomerDtoWhenCustomerExists() {
        // Given
        String customerId = "12345";
        CustomerDto mockCustomer = new CustomerDto(customerId, 100);
        when(customerServiceClient.getCustomer(customerId)).thenReturn(mockCustomer);

        // When
        CustomerDto result = customerService.getCustomer(customerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(customerId);
        assertThat(result.loyaltyPoints()).isEqualTo(100);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Given
        String customerId = "unknown";
        when(customerServiceClient.getCustomer(customerId))
                .thenThrow(new CustomerNotFoundException(customerId));

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomer(customerId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining(customerId);
    }
}