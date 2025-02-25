package com.sportygroup.orderservice.dto;

import java.util.List;

public record BooksDto(
        List<BookDto> books
) {
}
