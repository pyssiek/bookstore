package com.sportygroup.orderservice.client;

import com.sportygroup.orderservice.dto.CustomerDto;
import com.sportygroup.orderservice.exception.CustomerNotFoundException;
import com.sportygroup.orderservice.exception.ExternalServiceClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CustomerServiceClient {

    private final RestClient restClient;

    public CustomerServiceClient(RestClient.Builder restClientBuilder, @Value("${service.customer.url}") String bookServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(bookServiceUrl + "/api/v1/customers").build();
    }

    public CustomerDto getCustomer(String id) {
        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new ExternalServiceClientException("customer-service", response.getStatusCode().value());
                })
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new CustomerNotFoundException(id);
                })
                .body(CustomerDto.class);
    }

}
