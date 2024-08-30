package com.example.deal.integration;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

@Testcontainers
public class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("deal")
                .withUsername("deal_worker")
                .withPassword("deal_worker");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    static void runMigrations(JdbcDatabaseContainer<?> c) {
        try {
            String jdbcUrl = c.getJdbcUrl();
            String username = c.getUsername();
            String password = c.getPassword();

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Path changeLogPath =
                    new File(".").toPath().toAbsolutePath().getParent().getParent().resolve("deal/src/main/resources/migrations-deal");
            Liquibase liquibase = new Liquibase("master.xml", new DirectoryResourceAccessor(changeLogPath), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new RuntimeException("Failed to run Liquibase migrations", e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
