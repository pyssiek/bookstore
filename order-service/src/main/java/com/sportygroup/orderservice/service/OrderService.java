package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.dto.*;
import com.sportygroup.orderservice.exception.OrderNotFoundException;
import com.sportygroup.orderservice.mapper.OrderMapper;
import com.sportygroup.orderservice.model.Order;
import com.sportygroup.orderservice.model.OrderItem;
import com.sportygroup.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final BookService bookService;
    private final CustomerService customerService;
    private final DiscountService discountService;
    private final OrderValidationService orderValidationService;
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final OrderMapper orderMapper;

    public OrderDto getOrderById(String orderId) {
        return orderMapper.toDto(orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId)));
    }

    public OrderPageResponseDto getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderMapper.toOrderPageResponseDto(orderPage);
    }

    public OrderDto createOrder(OrderRequestDto orderRequest) {

        CustomerDto customer = customerService.getCustomer(orderRequest.customerId());

        List<String> orderItemsIds = orderRequest.items().stream().map(OrderItemRequestDto::id).toList();
        List<BookDto> books = bookService.getBooksByIds(orderItemsIds);
        Map<String, BookDto> booksMap = books.stream().collect(Collectors.toMap(BookDto::id, book -> book));

        orderValidationService.validateBooks(orderRequest.items(), booksMap);

        if (orderRequest.items().stream().anyMatch(OrderItemRequestDto::useLoyaltyPoints)) {
            orderValidationService.validateCustomerLoyaltyPoints(orderRequest.items(), customer);
        }

        Map<String, Integer> discounts = discountService.calculate(orderRequest.items(), booksMap);

        Order order = createOrder(orderRequest, customer, booksMap, discounts);
        order = orderRepository.save(order);

        OrderDto orderDto = orderMapper.toDto(order);
        orderEventPublisher.publishOrderEvent(orderDto);

        return orderDto;
    }

    private Order createOrder(OrderRequestDto orderRequest,
                              CustomerDto customer,
                              Map<String, BookDto> booksMap,
                              Map<String, Integer> discounts) {
        List<OrderItem> orderItems = orderRequest.items().stream().map(item -> {
            BookDto book = booksMap.get(item.id());
            int discount = discounts.get(item.id());
            BigDecimal discountFactor = BigDecimal.valueOf(1 - discount / 100.0);
            BigDecimal finalPrice = book.price().multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalPrice = finalPrice.multiply(BigDecimal.valueOf(item.quantity())).setScale(2, RoundingMode.HALF_UP);

            return new OrderItem(
                    item.id(),
                    book.title(),
                    book.isbn(),
                    book.type(),
                    item.quantity(),
                    book.price(),
                    discount,
                    item.useLoyaltyPoints(),
                    finalPrice,
                    totalPrice
            );
        }).toList();

        return new Order(
                null,
                customer.id(),
                Instant.now(),
                orderItems.stream().map(OrderItem::totalPrice).reduce(BigDecimal.ZERO, BigDecimal::add),
                orderItems
        );
    }
}
