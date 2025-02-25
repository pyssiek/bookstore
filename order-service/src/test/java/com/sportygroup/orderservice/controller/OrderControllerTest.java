package com.sportygroup.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.dto.OrderItemRequestDto;
import com.sportygroup.orderservice.dto.OrderPageResponseDto;
import com.sportygroup.orderservice.dto.OrderRequestDto;
import com.sportygroup.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // Given
        String orderId = "order1";
        OrderDto orderDto = getOrderDto();

        when(orderService.getOrderById(orderId)).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(get("/api/v1/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderDto.orderId()))
                .andExpect(jsonPath("$.customerId").value(orderDto.customerId()))
                .andExpect(jsonPath("$.totalAmount").value(orderDto.totalAmount().doubleValue()));

        verify(orderService).getOrderById(orderId); // Ensure service method is called
    }


    @Test
    void shouldReturnAllOrders() throws Exception {
        // Given
        OrderDto orderDto = getOrderDto();
        Page<OrderDto> orderPage = new PageImpl<>(List.of(orderDto), PageRequest.of(0, 20), 1);
        OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto(orderPage.getContent(), orderPage.getTotalElements(),
                orderPage.getTotalPages(), orderPage.getNumber() + 1, orderPage.getSize());

        when(orderService.getAllOrders(1, 20)).thenReturn(orderPageResponseDto);

        // When & Then
        mockMvc.perform(get("/api/v1/orders")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(orderDto.orderId()))
                .andExpect(jsonPath("$.totalElements").value(orderPage.getTotalElements()));

        verify(orderService).getAllOrders(1, 20);
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        OrderRequestDto orderRequestDto =
                new OrderRequestDto("customer1",
                        List.of(new OrderItemRequestDto("1", 2, false)));
        OrderDto orderDto = getOrderDto();
        when(orderService.createOrder(orderRequestDto)).thenReturn(orderDto);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/orders/order1"))
                .andReturn();

        verify(orderService).createOrder(orderRequestDto);
    }

    private OrderDto getOrderDto() {
        return new OrderDto("order1", "customer1", Instant.now(),
                BigDecimal.TEN, List.of());
    }
}