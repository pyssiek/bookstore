package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.client.BookServiceClient;
import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.BookType;
import com.sportygroup.orderservice.dto.BooksDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookServiceClient bookServiceClient;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookServiceClient);
    }

    @Test
    void shouldReturnBooksByIds() {
        // Given
        List<String> bookIds = List.of("1-1", "1");
        List<BookDto> expectedBooks = List.of(
                new BookDto("1", "Thinking in Java", "Bruce Eckel", "123456",
                        BookType.NEW_RELEASE, BigDecimal.TEN, 10),
                new BookDto("2", "Effective Java", "Joshua Bloch", "654321",
                        BookType.OLD_EDITION, BigDecimal.TEN, 5)
        );
        BooksDto responseDto = new BooksDto(expectedBooks);

        when(bookServiceClient.getBooksByIds(bookIds)).thenReturn(responseDto);

        // When
        List<BookDto> result = bookService.getBooksByIds(bookIds);

        // Then
        assertThat(expectedBooks).usingRecursiveComparison().isEqualTo(result);
        verify(bookServiceClient).getBooksByIds(bookIds);
    }
}