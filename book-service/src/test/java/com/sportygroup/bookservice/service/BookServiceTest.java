package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.exception.BookNotFoundException;
import com.sportygroup.bookservice.mapper.BookMapper;
import com.sportygroup.bookservice.model.Book;
import com.sportygroup.bookservice.model.BookType;
import com.sportygroup.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private BookDto bookDto;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto(1L, "Book Title", "Author", "1234567890",
                BookType.NEW_RELEASE, BigDecimal.valueOf(100), 10);
        book = new Book(1L, "Book Title", "Author", "1234567890", BookType.NEW_RELEASE,
                BigDecimal.valueOf(100), 10);
    }

    @Test
    void shouldCreateBook() {
        // Given
        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        // When
        long bookId = bookService.createBook(bookDto);

        // Then
        assertEquals(1L, bookId);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldGetBookById() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto result = bookService.getBookById(1L);

        // Then
        assertNotNull(result);
        assertEquals(bookDto, result);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
    }

    @Test
    void shouldGetBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toBookPageResponseDto(bookPage)).thenReturn
                (new BookPageResponseDto(List.of(bookDto), 1, 1, 1, 1));

        BookPageResponseDto result = bookService.getBooks(1, 10, Optional.empty());

        assertNotNull(result);
        assertEquals(1, result.books().size());
    }

    @Test
    void shouldUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto updatedBook = bookService.updateBook(1L, bookDto);

        assertNotNull(updatedBook);
        verify(bookMapper).updateEntity(bookDto, book);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldUpdateStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto updatedBook = bookService.updateStock(1L, 5);

        assertNotNull(updatedBook);
        verify(bookRepository).updateStock(1L, 5);
    }

    @Test
    void shouldDeleteBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        when(bookRepository.existsById(2L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(2L));
    }
}