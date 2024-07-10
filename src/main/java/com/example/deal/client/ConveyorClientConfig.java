package com.example.deal.client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConveyorClientConfig {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 10000, 1);
    }
}
