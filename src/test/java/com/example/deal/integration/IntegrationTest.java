package com.example.deal.integration;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationTest extends IntegrationEnvironment {
    @Test
    public void testConnection() {
        Assertions.assertTrue(POSTGRES.isRunning());
        assertThat(POSTGRES.getUsername()).isEqualTo("deal_worker");
        assertThat(POSTGRES.getPassword()).isEqualTo("deal_worker");
        assertThat(POSTGRES.getDatabaseName()).isEqualTo("deal");
    }
}
