package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class DatabaseInteractorTest {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");

    private DatabaseInteractor databaseInteractor;
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mySQLContainer.getJdbcUrl());
        hikariConfig.setUsername(mySQLContainer.getUsername());
        hikariConfig.setPassword(mySQLContainer.getPassword());
        dataSource = new HikariDataSource(hikariConfig);
        databaseInteractor = new DatabaseInteractor(dataSource);
    }

    @Test
    void testAddAndGetUser() throws SQLException {
        databaseInteractor.createUserTable();
        databaseInteractor.addUser(1, "John Doe");
        String userName = databaseInteractor.getUser(1);
        assertEquals("John Doe", userName);
    }

    @AfterEach
    void tearDown() throws SQLException {
        ((HikariDataSource) dataSource).close();
    }
}
