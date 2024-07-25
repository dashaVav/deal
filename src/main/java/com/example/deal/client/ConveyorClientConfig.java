package com.example.deal.client;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConveyorClientConfig {
    @Value("${feign-client.conveyor-client.retryer.period}")
    private long period;

    @Value("${feign-client.conveyor-client.retryer.max-period}")
    private long maxPeriod;

    @Value("${feign-client.conveyor-client.retryer.max-attempts}")
    private int maxAttempts;

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(period, maxPeriod, maxAttempts);
    }
}
