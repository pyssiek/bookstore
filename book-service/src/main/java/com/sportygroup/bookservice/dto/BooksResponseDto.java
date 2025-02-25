package com.sportygroup.bookservice.dto;

import java.util.List;

public record BooksResponseDto(
        List<BookDto> books
) {
}
