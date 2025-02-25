package com.sportygroup.bookservice.service;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.exception.BookNotFoundException;
import com.sportygroup.bookservice.mapper.BookMapper;
import com.sportygroup.bookservice.model.Book;
import com.sportygroup.bookservice.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public long createBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        return bookRepository.save(book).getId();
    }

    public BookDto getBookById(Long bookId) {
        return bookMapper.toDto(getBook(bookId));
    }


    public BookPageResponseDto getBooks(int page, int size, Optional<List<Long>> ids) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> bookPage = ids.isPresent() ?
                bookRepository.findByIdIn(ids.get(), pageable) :
                bookRepository.findAll(pageable);
        return bookMapper.toBookPageResponseDto(bookPage);
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = getBook(id);

        bookMapper.updateEntity(bookDto, book);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    public BookDto updateStock(Long bookId, int quantity) {
        bookRepository.updateStock(bookId, quantity);
        return getBookById(bookId);
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
        bookRepository.deleteById(bookId);
    }

    private Book getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
        return book;
    }

}
