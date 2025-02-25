package com.sportygroup.orderservice.exception;

import com.sportygroup.orderservice.dto.BookDto;
import com.sportygroup.orderservice.dto.BookType;

import java.util.List;
import java.util.stream.Collectors;

public class BookNotForFreeException extends BaseException {

    public BookNotForFreeException(List<BookDto> books, List<BookType> allowedBookTypes) {
        super(buildMessage(books, allowedBookTypes), "BOOK_NOT_FOR_FREE");
    }

    private static String buildMessage(List<BookDto> books, List<BookType> allowedBookTypes) {
        StringBuilder message = new StringBuilder("Books not allowed to be purchased with loyalty points: ");
        message.append(books.stream()
                .map(book -> book.id() + "(" + book.type() + ")")
                .collect(Collectors.joining(", "))
        );
        message.append(". Allowed book types: ");
        message.append(allowedBookTypes);
        return message.toString();
    }

}