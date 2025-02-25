package com.sportygroup.bookservice.mapper;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setType(dto.getType());
        book.setPrice(dto.getPrice());
        book.setStockQuantity(dto.getStockQuantity());
        return book;
    }

    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getType(),
                book.getPrice(),
                book.getStockQuantity()
        );
    }

    public void updateEntity(BookDto bookDto, Book book) {

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setType(bookDto.getType());
        book.setPrice(bookDto.getPrice());
        book.setStockQuantity(bookDto.getStockQuantity());
    }

    public BookPageResponseDto toBookPageResponseDto(Page<Book> bookPage) {
        List<BookDto> books = bookPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new BookPageResponseDto(
                books,
                bookPage.getTotalElements(),
                bookPage.getTotalPages(),
                bookPage.getNumber() + 1,
                bookPage.getSize()
        );
    }
}
