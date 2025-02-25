package com.sportygroup.orderservice.dto;

import java.util.List;

public record OrderPageResponseDto(
        List<OrderDto> orders,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {
}
