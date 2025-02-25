package com.sportygroup.bookservice.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {
    private final String errorCode = "BOOK_NOT_FOUND";

    public BookNotFoundException(Long id) {
        super("Book with ID " + id + " not found");
    }
}