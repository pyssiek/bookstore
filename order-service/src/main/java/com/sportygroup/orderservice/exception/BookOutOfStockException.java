package com.sportygroup.orderservice.exception;

import java.util.List;
import java.util.stream.Collectors;

public class BookOutOfStockException extends BaseException {

    public BookOutOfStockException(List<BookOutOfStock> booksOutOfStock) {
        super(buildMessage(booksOutOfStock), "BOOK_OUT_OF_STOCK");
    }

    private static String buildMessage(List<BookOutOfStock> booksOutOfStock) {
        return booksOutOfStock.stream()
                .map(book -> String.format("Book with ID %s is out of stock. Quantity required: %d, available: %d",
                        book.id(), book.requiredQuantity(), book.availableQuantity()))
                .collect(Collectors.joining(". "));
    }

}

