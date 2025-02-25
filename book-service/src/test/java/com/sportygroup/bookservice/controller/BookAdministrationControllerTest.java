package com.sportygroup.bookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.bookservice.dto.BookDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookAdministrationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookAdministrationController bookAdministrationController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookAdministrationController).build();
    }

    @Test
    void shouldCreateBook() throws Exception {
        // Given
        long bookId = 1L;
        BookDto bookDto = getBookDto();
        when(bookService.createBook(any())).thenReturn(bookId);

        // When & Then
        mockMvc.perform(post("/api/v1/admin/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/books/1"));

        verify(bookService, times(1)).createBook(any());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        // Given
        Long bookId = 1L;
        BookDto updatedBook = getBookDto();
        when(bookService.updateBook(eq(bookId), any())).thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(put("/api/v1/admin/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Refactoring"))
                .andExpect(jsonPath("$.author").value("Martin Fowler"))
                .andExpect(jsonPath("$.price").value(55.00));

        verify(bookService, times(1)).updateBook(eq(bookId), any());
    }


    @Test
    void shouldIncreaseStock() throws Exception {
        // Given
        Long bookId = 1L;
        int quantity = 5;
        BookDto updatedBook = getBookDto();
        when(bookService.updateStock(bookId, quantity)).thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(patch("/api/v1/admin/books/{bookId}/increase-stock", bookId)
                        .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(15));

        verify(bookService, times(1)).updateStock(bookId, quantity);
    }

    @Test
    void shouldDeleteBook() throws Exception {
        // Given
        Long bookId = 1L;
        doNothing().when(bookService).deleteBook(bookId);

        // When & Then
        mockMvc.perform(delete("/api/v1/admin/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(bookId);
    }

    private BookDto getBookDto() {
        return new BookDto(1L, "Refactoring", "Martin " +
                "Fowler", "9780201485677", BookType.NEW_RELEASE, new BigDecimal("55.00"), 15);
    }
}