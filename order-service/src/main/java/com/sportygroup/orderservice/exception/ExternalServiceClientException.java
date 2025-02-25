package com.sportygroup.orderservice.exception;

public class ExternalServiceClientException extends BaseException {

    public ExternalServiceClientException(String serviceName, int statusCode) {
        super(String.format("%s error. Response status code: %s", serviceName, statusCode),
                "EXTERNAL_SERVICE_ERROR");
    }

}