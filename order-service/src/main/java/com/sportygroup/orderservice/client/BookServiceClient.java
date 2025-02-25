package com.sportygroup.orderservice.client;

import com.sportygroup.orderservice.dto.BooksDto;
import com.sportygroup.orderservice.exception.ExternalServiceClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class BookServiceClient {
    private final RestClient restClient;

    public BookServiceClient(RestClient.Builder restClientBuilder, @Value("${service.book.url}") String bookServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(bookServiceUrl + "/api/v1/books").build();
    }

    public BooksDto getBooksByIds(List<String> ids) {
        return restClient.get()
                .uri("?size={size}&ids={ids}", ids.size(), String.join(",", ids))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new ExternalServiceClientException("book-service", response.getStatusCode().value());
                })
                .body(BooksDto.class);
    }

}
