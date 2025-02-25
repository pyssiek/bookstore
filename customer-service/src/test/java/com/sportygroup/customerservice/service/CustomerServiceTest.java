package com.sportygroup.customerservice.service;

import com.sportygroup.customerservice.dto.CustomerDto;
import com.sportygroup.customerservice.dto.CustomerPageResponseDto;
import com.sportygroup.customerservice.exception.CustomerNotFoundException;
import com.sportygroup.customerservice.mapper.CustomerMapper;
import com.sportygroup.customerservice.model.Customer;
import com.sportygroup.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;


    @Test
    void shouldGetCustomerById() {
        // Given
        String customerId = "customer1";
        CustomerDto customerDto = getCustomerDto();
        Customer customer = getCustomer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        CustomerDto result = customerService.getCustomerById(customerId);

        // Then
        assertThat(customerDto).usingRecursiveComparison().isEqualTo(result);
        verify(customerRepository).findById(customerId);
        verify(customerMapper).toDto(customer);
    }


    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCustomerNotFound() {
        // Given
        String customerId = "unknownCustomer";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.getCustomerById(customerId))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void shouldGetAllCustomers() {
        // Given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customerPage = new PageImpl<>(List.of(getCustomer()));
        CustomerPageResponseDto responseDto =
                new CustomerPageResponseDto(List.of(getCustomerDto()), 1, 1, page, size);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toCustomerPageResponseDto(customerPage)).thenReturn(responseDto);

        // When
        CustomerPageResponseDto result = customerService.getAllCustomers(page, size);

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(result);
        verify(customerRepository).findAll(pageable);
        verify(customerMapper).toCustomerPageResponseDto(customerPage);
    }

    @Test
    void shouldAddCustomer() {
        // Given
        CustomerDto customerDto = getCustomerDto();
        Customer customer = getCustomer();
        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        String customerId = customerService.addCustomer(customerDto);

        // Then
        assertThat(customerId).isEqualTo("customer1");
        verify(customerMapper).toEntity(customerDto);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldUpdateLoyaltyPoints() {
        // Given
        String customerId = "customer1";
        int points = 200;
        CustomerDto customerDto = getCustomerDto();
        Customer customer = getCustomer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        CustomerDto updatedCustomer = customerService.updateLoyaltyPoints(customerId, points);

        // Then
        assertThat(updatedCustomer).usingRecursiveComparison().isEqualTo(customerDto);
        verify(customerRepository).updateLoyaltyPoints(customerId, points);
        verify(customerMapper).toDto(customer);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenUpdatingLoyaltyPointsForNonExistentCustomer() {
        // Given
        String customerId = "unknownCustomer";
        int points = 200;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customerService.updateLoyaltyPoints(customerId, points))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    private Customer getCustomer() {
        return new Customer("customer1", "John", "Doe",
                "john@doe", 10, Collections.EMPTY_LIST);
    }

    private CustomerDto getCustomerDto() {
        return new CustomerDto("customer1", "John", "Doe",
                "john@doe", 10, Collections.EMPTY_LIST);
    }
}