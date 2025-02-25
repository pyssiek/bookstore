package com.sportygroup.orderservice.config;

import io.micrometer.observation.ObservationPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.ServerRequestObservationContext;

@Configuration
public class TracingConfig {

    @Bean
    public ObservationPredicate excludeActuator() {
        return (name, context) -> {
            if (context instanceof ServerRequestObservationContext srCtx) {
                return !srCtx.getCarrier().getRequestURI().contains("/actuator/");
            }
            return true;
        };
    }
}
