package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.dto.*;
import com.sportygroup.orderservice.exception.OrderNotFoundException;
import com.sportygroup.orderservice.mapper.OrderMapper;
import com.sportygroup.orderservice.model.Order;
import com.sportygroup.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private BookService bookService;
    @Mock
    private CustomerService customerService;
    @Mock
    private DiscountService discountService;
    @Mock
    private OrderValidationService orderValidationService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderEventPublisher orderEventPublisher;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnOrderById() {
        // Given
        String orderId = "order123";
        Order order = getOrder(orderId);
        OrderDto orderDto = getOrderDto(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // When
        OrderDto result = orderService.getOrderById(orderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(orderDto);
        verify(orderRepository).findById(orderId);
        verify(orderMapper).toDto(order);
    }


    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        String orderId = "order123";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining(orderId);

        verify(orderRepository).findById(orderId);
    }

    @Test
    void shouldReturnPaginatedOrders() {
        // Given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage = new PageImpl<>(List.of(getOrder("order123")));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toOrderPageResponseDto(orderPage)).thenReturn(
                new OrderPageResponseDto(List.of(), 1, 1, page, size));

        // When
        OrderPageResponseDto result = orderService.getAllOrders(page, size);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.totalElements()).isEqualTo(1);
        verify(orderRepository).findAll(pageable);
        verify(orderMapper).toOrderPageResponseDto(orderPage);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        String customerId = "cust123";
        OrderRequestDto orderRequest = new OrderRequestDto(customerId,
                List.of(new OrderItemRequestDto("1", 2, false)));
        CustomerDto customer = new CustomerDto(customerId, 100);
        BookDto book = getBookDto();
        Map<String, BookDto> booksMap = Map.of("1", book);
        Map<String, Integer> discounts = Map.of("1", 10);

        Order order = new Order(null, customerId, Instant.now(), BigDecimal.TEN, List.of());
        Order savedOrder = new Order("order123", customerId, Instant.now(), BigDecimal.TEN, List.of());
        OrderDto orderDto = new OrderDto("order123", customerId, Instant.now(), BigDecimal.TEN, List.of());

        when(customerService.getCustomer(customerId)).thenReturn(customer);
        when(bookService.getBooksByIds(List.of("1"))).thenReturn(List.of(book));
        when(discountService.calculate(orderRequest.items(), booksMap)).thenReturn(discounts);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(orderDto);

        // When
        OrderDto result = orderService.createOrder(orderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo("order123");

        verify(customerService).getCustomer(customerId);
        verify(bookService).getBooksByIds(List.of("1"));
        verify(orderValidationService).validateBooks(orderRequest.items(), booksMap);
        verify(discountService).calculate(orderRequest.items(), booksMap);
        verify(orderRepository).save(any(Order.class));
        verify(orderEventPublisher).publishOrderEvent(orderDto);
    }

    private OrderDto getOrderDto(String orderId) {
        return new OrderDto(orderId, "customer1", Instant.now(), BigDecimal.TEN, List.of());
    }

    private Order getOrder(String orderId) {
        return new Order(orderId, "customer1", Instant.now(), BigDecimal.TEN, List.of());
    }

    private BookDto getBookDto() {
        return new BookDto("1", "Effective Java", "Joshua " +
                "Bloch", "123456789", BookType.NEW_RELEASE, new BigDecimal(
                "145.99"), 5);
    }
}