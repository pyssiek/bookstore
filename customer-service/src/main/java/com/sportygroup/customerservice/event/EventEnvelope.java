package com.sportygroup.customerservice.event;

import org.slf4j.MDC;

public record EventEnvelope<T>(
        String traceId,
        T payload
) {
    public static <T> EventEnvelope<T> wrap(T payload) {
        String traceId = MDC.get("traceId");
        return new EventEnvelope<>(traceId, payload);
    }
}
