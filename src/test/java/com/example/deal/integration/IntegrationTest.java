package com.example.deal.integration;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IntegrationTest extends IntegrationEnvironment {
    @Test
    public void testConnection() {
        Assertions.assertTrue(postgres.isRunning());
        assertThat(postgres.getUsername()).isEqualTo("deal_worker");
        assertThat(postgres.getPassword()).isEqualTo("deal_worker");
        assertThat(postgres.getDatabaseName()).isEqualTo("deal");
    }
}
