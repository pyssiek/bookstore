package com.sportygroup.customerservice.controller;

import com.sportygroup.customerservice.dto.CustomerDto;
import com.sportygroup.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void shouldReturnBookById() throws Exception {
        // Given
        when(customerService.getCustomerById("customer1")).thenReturn(getCustomerDto());

        // When & Then
        mockMvc.perform(get("/api/v1/customers/{id}", "customer1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("customer1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@doe"))
                .andExpect(jsonPath("$.loyaltyPoints").value(10))
        ;
    }

    private CustomerDto getCustomerDto() {
        return new CustomerDto("customer1", "John", "Doe",
                "john@doe", 10, Collections.EMPTY_LIST);
    }
}