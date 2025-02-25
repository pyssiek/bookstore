package com.sportygroup.bookservice.controller;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.model.BookType;
import com.sportygroup.bookservice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void shouldReturnBookById() throws Exception {
        // Given
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(getBookDto());

        // When & Then
        mockMvc.perform(get("/api/v1/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.isbn").value("123456789"))
                .andExpect(jsonPath("$.type").value(BookType.NEW_RELEASE.name()))
                .andExpect(jsonPath("$.price").value("145.99"))
                .andExpect(jsonPath("$.stockQuantity").value("5"))
        ;
    }


    @Test
    void shouldReturnPaginatedBooks() throws Exception {
        // Given
        int page = 1, size = 20;
        List<BookDto> books = List.of(getBookDto());
        BookPageResponseDto bookPage = new BookPageResponseDto(books, 1L, 1, 1, size);

        when(bookService.getBooks(page, size, Optional.empty())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.books[0].id").value(1L))
                .andExpect(jsonPath("$.books[0].title").value("Effective Java"))
                .andExpect(jsonPath("$.books[0].author").value("Joshua Bloch"))
                .andExpect(jsonPath("$.books[0].isbn").value("123456789"))
                .andExpect(jsonPath("$.books[0].type").value(BookType.NEW_RELEASE.name()))
                .andExpect(jsonPath("$.books[0].price").value("145.99"))
                .andExpect(jsonPath("$.books[0].stockQuantity").value("5"));
    }

    private BookDto getBookDto() {
        return new BookDto(1L, "Effective Java", "Joshua " +
                "Bloch", "123456789", BookType.NEW_RELEASE, new BigDecimal(
                "145.99"), 5);
    }
}