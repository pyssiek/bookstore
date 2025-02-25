package com.sportygroup.bookservice.controller;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.dto.BookPageResponseDto;
import com.sportygroup.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<BookPageResponseDto> getBooks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Optional<List<Long>> ids) {
        return ResponseEntity.ok(bookService.getBooks(page, size, ids));
    }

}
