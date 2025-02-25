package com.sportygroup.orderservice.service;

import com.sportygroup.orderservice.client.BookServiceClient;
import com.sportygroup.orderservice.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookServiceClient bookServiceClient;

    public BookService(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
    }

    public List<BookDto> getBooksByIds(List<String> ids) {
        return bookServiceClient.getBooksByIds(ids).books();
    }
}
