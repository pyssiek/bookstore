package com.sportygroup.orderservice.controller;

import com.sportygroup.orderservice.dto.OrderDto;
import com.sportygroup.orderservice.dto.OrderPageResponseDto;
import com.sportygroup.orderservice.dto.OrderRequestDto;
import com.sportygroup.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<OrderPageResponseDto> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto,
                                            ServletUriComponentsBuilder uriBuilder) {
        OrderDto orderResponseDto = orderService.createOrder(orderRequestDto);
        URI location = uriBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderResponseDto.orderId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
