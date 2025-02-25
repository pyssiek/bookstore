package com.sportygroup.customerservice.dto;

import java.util.List;

public record CustomerPageResponseDto(
        List<CustomerDto> customers,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {
}