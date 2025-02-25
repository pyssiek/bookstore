package com.sportygroup.bookservice.dto;


import java.util.List;

public record BookPageResponseDto(
        List<BookDto> books,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {
}