package com.sportygroup.bookservice.controller;

import com.sportygroup.bookservice.dto.BookDto;
import com.sportygroup.bookservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/admin/books")
public class BookAdministrationController {

    private final BookService bookService;

    public BookAdministrationController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Void> createBook(@Valid @RequestBody BookDto bookDTO, UriComponentsBuilder uriBuilder) {
        long bookId = bookService.createBook(bookDTO);
        URI location =
                uriBuilder.path("/api/v1/books/{id}").buildAndExpand(bookId).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @PatchMapping("/{bookId}/increase-stock")
    public ResponseEntity<BookDto> increaseStock(
            @PathVariable Long bookId,
            @RequestParam int quantity) {

        BookDto updatedBook = bookService.updateStock(bookId, quantity);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}