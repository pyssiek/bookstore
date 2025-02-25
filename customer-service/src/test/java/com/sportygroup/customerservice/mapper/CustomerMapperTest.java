package com.sportygroup.customerservice.mapper;

import com.sportygroup.customerservice.dto.AddressDto;
import com.sportygroup.customerservice.dto.CustomerDto;
import com.sportygroup.customerservice.dto.CustomerPageResponseDto;
import com.sportygroup.customerservice.model.Address;
import com.sportygroup.customerservice.model.AddressType;
import com.sportygroup.customerservice.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    private final CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void shouldMapCustomerToCustomerDto() {
        // Given
        Customer customer = getCustomer();
        CustomerDto expectedCustomerDto = getCustomerDto();

        // When
        CustomerDto customerDto = customerMapper.toDto(customer);

        // Then
        assertThat(customerDto).isNotNull();
        assertThat(customerDto).usingRecursiveComparison().isEqualTo(customerDto);
    }

    @Test
    void shouldMapCustomerDtoToCustomer() {
        // Given
        CustomerDto customerDto = getCustomerDto();
        Customer expectedCustomer = getCustomer();

        // When
        Customer customer = customerMapper.toEntity(customerDto);

        // Then
        assertThat(customer).isNotNull();
        assertThat(customer).usingRecursiveComparison().isEqualTo(expectedCustomer);
    }

    @Test
    void shouldMapCustomerPageToCustomerPageResponseDto() {
        // Given
        Customer customer1 = getCustomer();
        Customer customer2 = getCustomer();

        Page<Customer> customerPage = new PageImpl<>(List.of(customer1, customer2), PageRequest.of(0, 2), 2);

        // When
        CustomerPageResponseDto responseDto = customerMapper.toCustomerPageResponseDto(customerPage);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.customers()).hasSize(2);
        assertThat(responseDto.totalElements()).isEqualTo(2);
        assertThat(responseDto.totalPages()).isEqualTo(1);
        assertThat(responseDto.pageSize()).isEqualTo(2);
        assertThat(responseDto.currentPage()).isEqualTo(1);
    }

    private Customer getCustomer() {
        return new Customer("1", "John", "Doe", "joh.@doe", 100,
                List.of(new Address("Street 1", "City", "12345", "Country", AddressType.HOME)));
    }

    private CustomerDto getCustomerDto() {
        return new CustomerDto("1", "John", "Doe", "joh.@doe", 100,
                List.of(new AddressDto("Street 1", "City", "12345", "Country", AddressType.HOME)));
    }
}