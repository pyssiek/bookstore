package com.sportygroup.bookservice.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String errorCode,
        String message,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String errorCode, String message) {
        this(status, errorCode, message, LocalDateTime.now());
    }
}