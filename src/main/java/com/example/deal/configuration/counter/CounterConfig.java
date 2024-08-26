package com.example.deal.configuration.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CounterConfig {
    @Value("${management.metrics.tags.application}")
    private String applicationName;

    @Value("${management.metrics.approved-application.name}")
    private String approvedApplicationMetricName;

    @Value("${management.metrics.credit-issues.name}")
    private String creditIssuedMetricName;

    @Bean
    public Counter approvedApplicationCounter(MeterRegistry registry) {
        return Counter.builder(approvedApplicationMetricName)
                .tag("application", applicationName)
                .register(registry);
    }

    @Bean
    public Counter creditIssuedCounter(MeterRegistry registry) {
        return Counter.builder(creditIssuedMetricName)
                .tag("application", applicationName)
                .register(registry);
    }
}
