package com.sportygroup.orderservice.exception;

import java.util.List;

public class BookNotFoundException extends BaseException {

    public BookNotFoundException(List<String> ids) {
        super("Books with IDs " + ids + " not found", "BOOK_NOT_FOUND");
    }
}